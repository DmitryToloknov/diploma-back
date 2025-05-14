package org.diploma.user.service;

import feign.FeignException;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.diploma.user.Util.Role;
import org.diploma.user.client.KeycloakAdminClient;
import org.diploma.user.client.KeycloakClient;
import org.diploma.user.client.dto.ClientBody;
import org.diploma.user.client.dto.CredentialKeycloak;
import org.diploma.user.client.dto.RequestRegisterKeycloak;
import org.diploma.user.client.dto.RequestUpdatePasswordKeycloak;
import org.diploma.user.client.dto.ResponseToken;
import org.diploma.user.client.dto.RoleResponse;
import org.diploma.user.controller.auth.dto.LoginRequest;
import org.diploma.user.controller.auth.dto.RegisterRequest;
import org.diploma.user.controller.auth.dto.UpdatePasswordRequest;
import org.diploma.user.exception.CustomException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.diploma.user.Util.GrantType.CLIENT_CREDENTIALS;
import static org.diploma.user.Util.GrantType.REFRESH_TOKEN;
import static org.diploma.user.exception.ExceptionMessageConstants.ERROR_GET_ROLES;
import static org.diploma.user.exception.ExceptionMessageConstants.ERROR_UPDATE_PASSWORD;
import static org.diploma.user.exception.ExceptionMessageConstants.ERROR_UPDATE_ROLES;
import static org.diploma.user.exception.ExceptionMessageConstants.LOGIN_ALREADY_EXIST;
import static org.diploma.user.exception.ExceptionMessageConstants.LOGIN_ERROR;
import static org.diploma.user.exception.ExceptionMessageConstants.TOKEN_NOT_VALID;
import static org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames.CLIENT_ID;
import static org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames.CLIENT_SECRET;
import static org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames.GRANT_TYPE;
import static org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames.TOKEN;

@Getter
@Setter
@Service
@Slf4j
@RequiredArgsConstructor
public class ClientService {

  private final KeycloakClient keycloakClient;
  private final KeycloakAdminClient keycloakAdminClient;

  private static final String BEARER = "Bearer ";
  private static final String USERNAME = "username";
  private static final String PASSWORD = "password";

  private static Map<String, RoleResponse> roles;

  private String token;
  @Value("${keycloak.client-id}")
  private String clientId;
  @Value("${keycloak.client-secret}")
  private String clientSecret;
  private ClientBody clientBody;

  @PostConstruct
  public void authClient() {
    refreshToken();
    loadRoles();
  }

  public void updateRole(List<String> newRoles, UUID userId) {
    log.info("Обновление ролей для пользователя {}", userId);
    var actualRoles = getRolesByUserId(userId);

    Set<String> newRoleSet = new HashSet<>(newRoles);
    Set<String> actualRoleSet = new HashSet<>(actualRoles);

    Set<String> rolesToAdd = new HashSet<>(newRoleSet);
    rolesToAdd.removeAll(actualRoleSet);

    Set<String> rolesToRemove = new HashSet<>(actualRoleSet);
    rolesToRemove.removeAll(newRoleSet);

    var fullRoleToAdd = rolesToAdd.stream()
        .map(s -> roles.get(s))
        .toList();

    var fullRoleToRemove = rolesToRemove.stream()
        .map(s -> roles.get(s))
        .toList();

    try {
      var responseAddRole = keycloakAdminClient.addRoles(getToken(), fullRoleToAdd, userId);
      var responseRemoveRole = keycloakAdminClient.deleteRoles(getToken(), fullRoleToRemove, userId);

      if(!responseAddRole.getStatusCode().is2xxSuccessful() || !responseRemoveRole.getStatusCode().is2xxSuccessful()) {
        throw new RuntimeException();
      }
    } catch (Exception e) {
      throw new CustomException(ERROR_UPDATE_ROLES);
    }

  }

  public void loadRoles() {
    log.info("Получение всех ролей.");
    var response = keycloakAdminClient.getRoles(getToken());
    if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
      var responseRoles = response.getBody();
      roles = filterRoles(responseRoles).stream()
          .collect(Collectors.toMap(
              RoleResponse::getName,
              role -> role
          ));
      log.info("Роли сохранны.({})", roles.size());
    } else {
      throw new RuntimeException("Ошибка получения ролей!");
    }
  }

  private List<RoleResponse> filterRoles(List<RoleResponse> roleResponses) {
    if (roleResponses == null) {
      return List.of();
    }
    var actualRoles = Role.getNameRoles();
    return roleResponses.stream()
        .filter(role -> actualRoles.contains(role.getName()))
        .toList();
  }

  public List<String> getRolesByUserId(UUID userId) {
    try {
      log.info("Получение ролей для пользователя {}", userId);
      var response = keycloakAdminClient.getRolesForUser(getToken(), userId);
      if (response.getStatusCode().is2xxSuccessful()) {
        return filterRoles(response.getBody()).stream()
            .map(RoleResponse::getName)
            .toList();
      } else {
        throw new RuntimeException();
      }
    } catch (Exception e) {
      throw new CustomException(ERROR_GET_ROLES);
    }
  }

  @Scheduled(fixedRate = 4 * 60 * 1000)
  public void refreshToken() {
    try {
      log.info("Обновление токена...");
      var response = keycloakClient.token(getData(CLIENT_CREDENTIALS));
      token = BEARER + response.getAccessToken();
      log.info("Токен обновился");
    } catch (Exception e) {
      log.error("Ошибка при обновлении токена", e);
    }
  }

  public UUID register(RegisterRequest requestRegister) {
    var password = new CredentialKeycloak();
    password.setValue(requestRegister.getPassword());

    var requestKeycloakForRegister = new RequestRegisterKeycloak();
    requestKeycloakForRegister.setCredentials(List.of(password));
    requestKeycloakForRegister.setUsername(requestRegister.getLogin());
    try {
      var responseEntity = keycloakAdminClient.registerUser(requestKeycloakForRegister, getToken());
      var path = responseEntity.getHeaders().getLocation().getPath().split("/");
      return UUID.fromString(path[path.length - 1]);
    } catch (FeignException.Conflict e) {
      throw new CustomException(LOGIN_ALREADY_EXIST);
    }
  }

  public void updatePassword(UpdatePasswordRequest updatePasswordRequest, UUID id) {
    var password = new CredentialKeycloak();
    password.setValue(updatePasswordRequest.getPassword());

    RequestUpdatePasswordKeycloak requestUpdatePasswordKeycloak = new RequestUpdatePasswordKeycloak();
    requestUpdatePasswordKeycloak.setCredentials(List.of(password));

    try {
      keycloakAdminClient.updatePassword(requestUpdatePasswordKeycloak, getToken(), id);
    } catch (Exception e) {
      throw new CustomException(ERROR_UPDATE_PASSWORD);
    }

  }

  public ResponseToken login(LoginRequest requestLogin) {
    try {
      var data = getData(PASSWORD);
      data.put(USERNAME, requestLogin.getLogin());
      data.put(PASSWORD, requestLogin.getPassword());
      return keycloakClient.token(data);
    } catch (FeignException e) {
      throw new CustomException(LOGIN_ERROR);
    }
  }

  public boolean validate(String token) {
    Map<String, String> data = new HashMap<>();
    data.put(CLIENT_ID, clientId);
    data.put(CLIENT_SECRET, clientSecret);
    data.put(TOKEN, token);
    return keycloakClient.validate(data).getStatus();
  }

  public ResponseToken refresh(String token) {
    try {
      var data = getData(REFRESH_TOKEN);
      data.put(REFRESH_TOKEN, token);
      return keycloakClient.token(data);
    } catch (FeignException.Unauthorized e) {
      throw new CustomException(TOKEN_NOT_VALID);
    }
  }

  public void revoke(String token) {
    Map<String, String> data = new HashMap<>();
    data.put(CLIENT_ID, clientId);
    data.put(CLIENT_SECRET, clientSecret);
    data.put(TOKEN, token);
    keycloakClient.revoke(data);
  }

  private Map<String, String> getData(String grantType) {
    Map<String, String> data = new HashMap<>();
    data.put(GRANT_TYPE, grantType);
    data.put(CLIENT_ID, clientId);
    data.put(CLIENT_SECRET, clientSecret);
    return data;
  }

}

package org.diploma.user.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.diploma.user.client.dto.ResponseToken;
import org.diploma.user.controller.auth.dto.AccessTokenResponse;
import org.diploma.user.controller.auth.dto.LoginRequest;
import org.diploma.user.controller.auth.dto.RegisterRequest;
import org.diploma.user.controller.auth.dto.UpdatePasswordRequest;
import org.diploma.user.exception.CustomException;
import org.diploma.user.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.UUID;

import static org.diploma.user.exception.ExceptionMessageConstants.GROUP_NOT_FOUND;
import static org.diploma.user.exception.ExceptionMessageConstants.LOGIN_ERROR;
import static org.diploma.user.exception.ExceptionMessageConstants.TOKEN_NOT_VALID;
import static org.diploma.user.exception.ExceptionMessageConstants.TYPE_NOT_FOUND;
import static org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames.REFRESH_TOKEN;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

  private final ClientService clientService;
  private final UserService userService;
  private final GroupService groupService;
  private final UserTypeService userTypeService;

  private final UserMapper userMapper;

  @Value("${server.url}")
  private String url;

  public URI registerUser(RegisterRequest requestRegister, UUID creatorId) {
    var user = userMapper.toUser(requestRegister);
    if (requestRegister.getGroupId() != null) {
      var group = groupService.findById(requestRegister.getGroupId()).orElseThrow(() -> new CustomException(GROUP_NOT_FOUND));
      user.setGroup(group);
    }
    userService.findById(creatorId).ifPresent(user::setCreator);
    if (requestRegister.getType() != null) {
      var types = userTypeService.findAllByIdIn(requestRegister.getType());
      if (types.size() != requestRegister.getType().size()) {
        throw new CustomException(TYPE_NOT_FOUND);
      }
      user.setTypes(types);
    }
    var keycloakId = clientService.register(requestRegister);
    user.setId(keycloakId);
    userService.save(user);
    return URI.create(url + "/users/" + keycloakId);
  }

  public void updatePassword(UUID id, UpdatePasswordRequest updatePasswordRequest) {
    clientService.updatePassword(updatePasswordRequest, id);
  }

  public ResponseEntity<AccessTokenResponse> login(LoginRequest requestLogin) {
    if (userService.findByLogin(requestLogin.getLogin()).isEmpty()) {
      throw new CustomException(LOGIN_ERROR);
    }

    var responseToken = clientService.login(requestLogin);
    return addCookie(responseToken);
  }

  public ResponseEntity<AccessTokenResponse> refresh(HttpServletRequest request) {
    var refreshToken = searchToken(request);
    if (refreshToken == null || !clientService.validate(refreshToken)) {
      throw new CustomException(TOKEN_NOT_VALID);
    }
    var responseToken = clientService.refresh(refreshToken);

    return addCookie(responseToken);
  }

  public ResponseEntity<String> revoke(String accessToken, HttpServletRequest request) {
    var refreshToken = searchToken(request);
    clientService.revoke(accessToken);
    clientService.revoke(refreshToken);
    return removeCookie();
  }

  private String searchToken(HttpServletRequest request) {
    String refreshToken = null;
    Cookie[] cookies = request.getCookies();
    if (cookies != null) {
      for (Cookie cookie : cookies) {
        if (REFRESH_TOKEN.equals(cookie.getName())) {
          refreshToken = cookie.getValue();
          break;
        }
      }
    }
    return refreshToken;
  }

  private ResponseEntity<AccessTokenResponse> addCookie(ResponseToken responseToken) {
    ResponseCookie refreshTokenCookie = ResponseCookie.from(REFRESH_TOKEN, responseToken.getRefreshToken())
        .httpOnly(true)
        .secure(false)
        .path("/")
        .maxAge(30 * 24 * 60 * 60)
//        .sameSite("")
        .build();

    var responseAccessToken = new AccessTokenResponse();
    responseAccessToken.setAccessToken(responseToken.getAccessToken());
    return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
        .body(responseAccessToken);
  }

  private ResponseEntity<String> removeCookie() {
    ResponseCookie refreshTokenCookie = ResponseCookie.from(REFRESH_TOKEN, "")
        .httpOnly(true)
        .secure(false)
        .path("/")
        .maxAge(0)
//        .sameSite("")
        .build();

    return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString()).build();
  }

}

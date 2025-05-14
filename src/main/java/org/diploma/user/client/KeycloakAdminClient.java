package org.diploma.user.client;

import org.diploma.user.client.dto.RequestRegisterKeycloak;
import org.diploma.user.client.dto.RequestUpdatePasswordKeycloak;
import org.diploma.user.client.dto.RoleResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "keycloakAdmin", url = "${keycloak.host-admin}")
public interface KeycloakAdminClient {

  @PostMapping("/users")
  ResponseEntity registerUser(@RequestBody RequestRegisterKeycloak requestRegister,
                              @RequestHeader("Authorization") String tokenClient);

  @PutMapping("/users/{id}")
  ResponseEntity updatePassword(@RequestBody RequestUpdatePasswordKeycloak requestUpdatePasswordKeycloak,
                                @RequestHeader("Authorization") String tokenClient, @PathVariable UUID id);

  @GetMapping("/roles")
  ResponseEntity<List<RoleResponse>> getRoles(@RequestHeader("Authorization") String tokenClient);

  @GetMapping("/users/{id}/role-mappings/realm")
  ResponseEntity<List<RoleResponse>> getRolesForUser(@RequestHeader("Authorization") String tokenClient,
                                                     @PathVariable UUID id);

  @PostMapping("/users/{id}/role-mappings/realm")
  ResponseEntity addRoles(@RequestHeader("Authorization") String tokenClient,
                          @RequestBody List<RoleResponse> roleResponses, @PathVariable UUID id);

  @DeleteMapping("/users/{id}/role-mappings/realm")
  ResponseEntity deleteRoles(@RequestHeader("Authorization") String tokenClient,
                             @RequestBody List<RoleResponse> roleResponses, @PathVariable UUID id);

}

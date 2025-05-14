package org.diploma.user.controller.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import java.net.URI;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.diploma.user.controller.auth.dto.LoginRequest;
import org.diploma.user.controller.auth.dto.RegisterRequest;
import org.diploma.user.controller.auth.dto.TokenRequest;
import org.diploma.user.controller.auth.dto.AccessTokenResponse;
import org.diploma.user.controller.auth.dto.UpdatePasswordRequest;
import org.diploma.user.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("auth")
public class AuthController {

  private final AuthService authService;

  @PostMapping("/login")
  public ResponseEntity<AccessTokenResponse> login(@RequestBody LoginRequest requestLogin) {
    return authService.login(requestLogin);
  }

  @PostMapping("/register")
  @PreAuthorize("hasRole('USER_UPDATE') || hasRole('ADMIN')")
  public ResponseEntity<URI> registerUser(@RequestBody @Valid RegisterRequest requestRegister,
                                          Authentication authentication) {
    var uri = authService.registerUser(requestRegister, UUID.fromString(authentication.getName()));
    return ResponseEntity.created(uri).build();
  }

  @PostMapping("/{id}/update-password")
  @PreAuthorize("hasRole('USER_UPDATE') || hasRole('ADMIN')")
  public ResponseEntity updatePassword(@RequestBody @Valid UpdatePasswordRequest updatePasswordRequest, @PathVariable UUID id) {
    authService.updatePassword(id, updatePasswordRequest);
    return ResponseEntity.ok().build();
  }

  @PostMapping("/refresh")
  public ResponseEntity<AccessTokenResponse> refresh(HttpServletRequest request) {
    return authService.refresh(request);
  }

  @PostMapping("/revoke")
  public ResponseEntity<String> revoke(@RequestBody TokenRequest requestToken, HttpServletRequest request) {
    return authService.revoke(requestToken.getToken(), request);
  }

}

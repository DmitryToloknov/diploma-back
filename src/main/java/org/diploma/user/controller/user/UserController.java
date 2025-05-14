package org.diploma.user.controller.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.diploma.user.controller.user.dto.FullNameDto;
import org.diploma.user.controller.user.dto.ResponseCurator;
import org.diploma.user.controller.user.dto.ResponseUser;
import org.diploma.user.controller.user.dto.RoleResponseForUser;
import org.diploma.user.controller.user.dto.UpdateUserRequest;
import org.diploma.user.controller.user.dto.UserDeleteRequest;
import org.diploma.user.controller.user.dto.UserFilterRequest;
import org.diploma.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("user")
@RequiredArgsConstructor
public class UserController {
  private final UserService userService;

  @GetMapping("/full-name")
  public ResponseEntity<FullNameDto> getFullName(Authentication authentication) {
    return ResponseEntity.ok(userService.getFullName(UUID.fromString(authentication.getName())));
  }

  @GetMapping("/curators")
  @PreAuthorize("hasRole('ADMIN')|| hasRole('GROUP_UPDATE')")
  public ResponseEntity<List<ResponseCurator>> getCurators() {
    return ResponseEntity.ok(userService.getCurators());
  }

  @PostMapping("/count")
  @PreAuthorize("hasRole('ADMIN')|| hasRole('USER_UPDATE')")
  public ResponseEntity<Long> numberUsers(@RequestBody UserFilterRequest userFilterRequest) {
    return ResponseEntity.ok(userService.numberUsers(userFilterRequest));
  }

  @PostMapping("/{page}/{perPage}")
  @PreAuthorize("hasRole('ADMIN')|| hasRole('USER_UPDATE')")
  public ResponseEntity<List<ResponseUser>> getUsers(@RequestBody UserFilterRequest userFilterRequest,
                                                     @PathVariable int page, @PathVariable int perPage) {
    return ResponseEntity.ok(userService.getUsers(userFilterRequest, page, perPage));
  }

  @GetMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')|| hasRole('USER_UPDATE')")
  public ResponseEntity<ResponseUser> getUserById(@PathVariable UUID id) {
    return ResponseEntity.ok(userService.getUserById(id));
  }

  @PutMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')|| hasRole('USER_UPDATE')")
  public ResponseEntity updateUser(@PathVariable UUID id, @RequestBody @Valid UpdateUserRequest updateUserRequest) {
    userService.updateUser(id, updateUserRequest);
    return ResponseEntity.ok().build();
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')|| hasRole('USER_UPDATE')")
  public ResponseEntity deleteUser(@RequestBody @Valid UserDeleteRequest userDeleteRequest, @PathVariable UUID id) {
    userService.deleteById(id, userDeleteRequest);
    return ResponseEntity.ok().build();
  }

  @GetMapping("/{id}/roles")
  @PreAuthorize("hasRole('ADMIN')|| hasRole('USER_UPDATE')")
  public ResponseEntity<List<String>> getRolesByUserId(@PathVariable UUID id) {
    return ResponseEntity.ok(userService.getRolesUserById(id));
  }

  @GetMapping("/roles")
  @PreAuthorize("hasRole('ADMIN')|| hasRole('USER_UPDATE')")
  public ResponseEntity<List<RoleResponseForUser>> getFullRoles() {
    return ResponseEntity.ok(userService.getFullRoles());
  }

  @PutMapping("/{id}/roles")
  @PreAuthorize("hasRole('ADMIN')|| hasRole('USER_UPDATE')")
  public ResponseEntity updateRoles(@PathVariable UUID id, @RequestBody List<String> roles) {
    userService.updateRoles(roles, id);
    return ResponseEntity.ok().build();
  }

  @GetMapping("/group/{id}")
  @PreAuthorize("hasRole('ADMIN')|| hasRole('COURSE_UPDATE')")
  public ResponseEntity getUsers(@PathVariable UUID id) {
    return ResponseEntity.ok(userService.getUsersByGroupId(id));
  }

}

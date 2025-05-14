package org.diploma.user.controller.userType;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.diploma.user.controller.userType.dto.UserTypeInfoResponse;
import org.diploma.user.controller.userType.dto.UserTypeRequest;
import org.diploma.user.controller.userType.dto.UserTypeResponse;
import org.diploma.user.service.UserTypeService;
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
@RequiredArgsConstructor
@RequestMapping("user-type")
public class UserTypeController {

  private final UserTypeService userTypeService;

  @GetMapping
  @PreAuthorize("hasRole('USER_UPDATE') || hasRole('ADMIN')")
  public ResponseEntity<List<UserTypeResponse>> findAll() {
    return ResponseEntity.ok(userTypeService.searchUserTypeResponse());
  }

//  @GetMapping("/{id}")
//  @PreAuthorize("hasRole('USER_TYPE_INFO') || hasRole('ADMIN')")
//  public ResponseEntity<UserTypeInfoResponse> findAll(@PathVariable UUID id) {
//    return ResponseEntity.ok(userTypeService.searchUserTypeInfoResponse(id));
//  }

  @PostMapping
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity createUserType(@RequestBody @Valid UserTypeRequest userTypeRequest,
                                       Authentication authentication) {
    var uri = userTypeService.createUserType(userTypeRequest, UUID.fromString(authentication.getName()));
    return ResponseEntity.created(uri).build();
  }

//  @PutMapping("/{id}")
//  @PreAuthorize("hasRole('USER_TYPE_UPDATE') || hasRole('ADMIN')")
//  public ResponseEntity updateUserType(@RequestBody @Valid UserTypeRequest userTypeRequest,
//                                       @PathVariable UUID id) {
//    userTypeService.updateUserType(userTypeRequest, id);
//    return ResponseEntity.ok().build();
//  }

//  @DeleteMapping("/{id}")
//  @PreAuthorize("hasRole('USER_TYPE_DELETE') || hasRole('ADMIN')")
//  public ResponseEntity deleteGroup(@PathVariable UUID id) {
//    userTypeService.deleteById(id);
//    return ResponseEntity.noContent().build();
//  }

}

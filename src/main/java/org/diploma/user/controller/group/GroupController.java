package org.diploma.user.controller.group;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.diploma.user.controller.group.dto.GroupInfoResponse;
import org.diploma.user.controller.group.dto.GroupRequest;
import org.diploma.user.controller.group.dto.GroupResponse;
import org.diploma.user.service.GroupService;
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
@RequestMapping("group")
public class GroupController {

  private final GroupService groupService;

  @GetMapping
  @PreAuthorize("hasRole('USER_UPDATE') || hasRole('ADMIN')")
  public ResponseEntity<List<GroupResponse>> findAll() {
    return ResponseEntity.ok(groupService.searchGroupResponseList());
  }

  @GetMapping("/setting")
  @PreAuthorize("hasRole('GROUP_UPDATE') || hasRole('ADMIN') || hasRole('COURSE_UPDATE')")
  public ResponseEntity<List<GroupInfoResponse>> getGroupsForSetting() {
    return ResponseEntity.ok(groupService.getGroupsForSetting());
  }

  @GetMapping("/{id}")
  @PreAuthorize("hasRole('GROUP_UPDATE') || hasRole('ADMIN')")
  public ResponseEntity<GroupInfoResponse> findById(@PathVariable UUID id) {
    return ResponseEntity.ok(groupService.searchGroupInfoResponse(id));
  }

  @PostMapping
  @PreAuthorize("hasRole('GROUP_UPDATE') || hasRole('ADMIN')")
  public ResponseEntity createGroup(@RequestBody @Valid GroupRequest groupRequest,
                                         Authentication authentication) {
    var uri = groupService.createGroup(groupRequest, UUID.fromString(authentication.getName()));
    return ResponseEntity.created(uri).build();
  }

  @PutMapping("/{id}")
  @PreAuthorize("hasRole('GROUP_UPDATE') || hasRole('ADMIN')")
  public ResponseEntity updateGroup(@RequestBody @Valid GroupRequest groupRequest,
                                         @PathVariable UUID id) {
    groupService.updateGroup(groupRequest, id);
    return ResponseEntity.ok().build();
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('GROUP_UPDATE') || hasRole('ADMIN')")
  public ResponseEntity deleteGroup(@PathVariable UUID id) {
    groupService.deleteById(id);
    return ResponseEntity.noContent().build();
  }

}

package org.diploma.user.controller.attempt;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.diploma.user.controller.attempt.dto.AttemptInfo;
import org.diploma.user.controller.attempt.dto.AttemptRequest;
import org.diploma.user.controller.attempt.dto.ReasonApproveAttempt;
import org.diploma.user.controller.attempt.dto.ReasonRejectAttempt;
import org.diploma.user.controller.attempt.dto.ResponseTaskInfoForCourse;
import org.diploma.user.service.AttemptService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/attempt")
public class AttemptController {

  private final AttemptService attemptService;

  @PostMapping("/{taskId}/{courseId}")
  public ResponseEntity addAttempt(@RequestBody @Valid AttemptRequest attemptRequest,
                                   @PathVariable Long taskId,
                                   @PathVariable UUID courseId,
                                   Authentication authentication) {
    attemptService.createAttempt(taskId, courseId, attemptRequest, authentication);
    return ResponseEntity.ok().build();
  }

  @GetMapping("/{taskId}/{courseId}")
  public ResponseEntity getAttempt(@PathVariable Long taskId,
                                   @PathVariable UUID courseId,
                                   Authentication authentication) {
    return ResponseEntity.ok(attemptService.searchAttempt(taskId, courseId, authentication));
  }

  @GetMapping("/{taskId}/{courseId}/{userId}")
  @PreAuthorize("hasRole('COURSE_UPDATE') || hasRole('ADMIN')")
  public ResponseEntity getAttempt(@PathVariable Long taskId,
                                   @PathVariable UUID courseId,
                                   @PathVariable UUID userId) {
    return ResponseEntity.ok(attemptService.searchAttempt(taskId, courseId, userId));
  }

  @GetMapping("/{id}")
  public ResponseEntity<AttemptInfo> findById(@PathVariable UUID id) {
    return ResponseEntity.ok(attemptService.findHistory(id));
  }

  @PostMapping("{id}/reject")
  @PreAuthorize("hasRole('COURSE_UPDATE') || hasRole('ADMIN')")
  public ResponseEntity rejectById(@PathVariable UUID id, @RequestBody @Valid ReasonRejectAttempt reasonAttempt) {
    attemptService.rejectById(id, reasonAttempt);
    return ResponseEntity.ok().build();
  }

  @PostMapping("{id}/approve")
  @PreAuthorize("hasRole('COURSE_UPDATE') || hasRole('ADMIN')")
  public ResponseEntity approveById(@PathVariable UUID id, @RequestBody ReasonApproveAttempt reasonApproveAttempt) {
    attemptService.approveById(id, reasonApproveAttempt);
    return ResponseEntity.ok().build();
  }

  @GetMapping("/course/{courseId}")
  public ResponseEntity<ResponseTaskInfoForCourse> findTaskInfo(@PathVariable UUID courseId, Authentication authentication) {
    return ResponseEntity.ok(attemptService.findTaskInfo(courseId, authentication));
  }

  @GetMapping("/course/{courseId}/{userId}")
  @PreAuthorize("hasRole('COURSE_UPDATE') || hasRole('ADMIN')")
  public ResponseEntity<ResponseTaskInfoForCourse> findTaskInfo(@PathVariable UUID courseId, @PathVariable UUID userId) {
    return ResponseEntity.ok(attemptService.findTaskInfo(courseId, userId));
  }

}

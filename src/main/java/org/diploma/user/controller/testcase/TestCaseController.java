package org.diploma.user.controller.testcase;

import lombok.RequiredArgsConstructor;
import org.diploma.user.controller.testcase.dto.RequestTestCase;
import org.diploma.user.controller.testcase.dto.ResponseTestCase;
import org.diploma.user.service.TestCaseService;
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
@RequestMapping("test-case")
public class TestCaseController {

  private final TestCaseService testCaseService;

  @PostMapping("/{taskId}")
  @PreAuthorize("hasRole('COURSE_UPDATE') || hasRole('ADMIN')")
  public ResponseEntity createTestCase(@PathVariable Long taskId,  Authentication authentication) {
    testCaseService.createTestCase(taskId, UUID.fromString(authentication.getName()));
    return ResponseEntity.ok().build();
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('COURSE_UPDATE') || hasRole('ADMIN')")
  public ResponseEntity createTestCase(@PathVariable UUID id) {
    testCaseService.deleteTestCase(id);
    return ResponseEntity.ok().build();
  }

  @GetMapping("/task/{taskId}")
  @PreAuthorize("hasRole('COURSE_UPDATE') || hasRole('ADMIN')")
  public ResponseEntity<List<ResponseTestCase>> createTestCase(@PathVariable Long taskId) {
    return ResponseEntity.ok(testCaseService.getTestCase(taskId));
  }

  @GetMapping("/{id}")
  @PreAuthorize("hasRole('COURSE_UPDATE') || hasRole('ADMIN')")
  public ResponseEntity<ResponseTestCase> getTestCase(@PathVariable UUID id) {
    return ResponseEntity.ok(testCaseService.getTestCase(id));
  }

  @PutMapping
  @PreAuthorize("hasRole('COURSE_UPDATE') || hasRole('ADMIN')")
  public ResponseEntity updateTestCase(@RequestBody RequestTestCase requestTestCase) {
    testCaseService.updateTestCase(requestTestCase);
    return ResponseEntity.ok().build();
  }

}

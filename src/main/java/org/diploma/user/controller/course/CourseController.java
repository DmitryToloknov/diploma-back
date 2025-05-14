package org.diploma.user.controller.course;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.diploma.user.controller.course.dto.CourseInfo;
import org.diploma.user.controller.course.dto.FilterCourse;
import org.diploma.user.controller.course.dto.ResponseCourse;
import org.diploma.user.controller.course.dto.ResponseCourseInfo;
import org.diploma.user.controller.task.dto.ResponseTaskInfo;
import org.diploma.user.service.CourseService;
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
@RequestMapping("/course")
public class CourseController {

  private final CourseService courseService;

  @PostMapping
  @PreAuthorize("hasRole('COURSE_UPDATE') || hasRole('ADMIN')")
  public ResponseEntity<UUID> createCourse(Authentication authentication) {
    return ResponseEntity.ok(courseService.createCourse(UUID.fromString(authentication.getName())));
  }

  @GetMapping("/{id}")
  @PreAuthorize("hasRole('COURSE_UPDATE') || hasRole('ADMIN')")
  public ResponseEntity<CourseInfo> createCourse(@PathVariable UUID id) {
    return ResponseEntity.ok(courseService.getCourseInfo(id));
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('COURSE_UPDATE') || hasRole('ADMIN')")
  public ResponseEntity deleteCourse(@PathVariable UUID id) {
    courseService.deleteCourse(id);
    return ResponseEntity.ok().build();
  }

  @PutMapping
  @PreAuthorize("hasRole('COURSE_UPDATE') || hasRole('ADMIN')")
  public ResponseEntity updateCourse(@RequestBody @Valid CourseInfo courseInfo) {
    courseService.updateCourse(courseInfo);
    return ResponseEntity.ok().build();
  }

  @PostMapping("/{id}/{taskId}")
  @PreAuthorize("hasRole('COURSE_UPDATE') || hasRole('ADMIN')")
  public ResponseEntity addTaskInCourse(@PathVariable UUID id, @PathVariable Long taskId) {
    courseService.addTask(id, taskId);
    return ResponseEntity.ok().build();
  }

  @GetMapping("/{id}/tasks")
  @PreAuthorize("hasRole('COURSE_UPDATE') || hasRole('ADMIN')")
  public ResponseEntity<List<ResponseTaskInfo>> getTasksInfo(@PathVariable UUID id) {
    return ResponseEntity.ok(courseService.getTasksInfo(id));
  }

  @DeleteMapping("/{id}/{taskId}")
  @PreAuthorize("hasRole('COURSE_UPDATE') || hasRole('ADMIN')")
  public ResponseEntity removeTaskInCourse(@PathVariable UUID id, @PathVariable Long taskId) {
    courseService.removeTask(id, taskId);
    return ResponseEntity.ok().build();
  }

  @PostMapping("/total-number")
  public ResponseEntity<Long> getTotalNumber(@RequestBody FilterCourse filterCourse, Authentication authentication) {
    return ResponseEntity.ok(courseService.getCourseNumber(filterCourse, authentication));
  }

  @PostMapping("/page/{page}/{perPage}")
  public ResponseEntity<List<ResponseCourse>> getTotalNumber(@RequestBody FilterCourse filterCourse,
                                                             Authentication authentication,
                                                             @PathVariable Long page,
                                                             @PathVariable Long perPage) {
    return ResponseEntity.ok(courseService.getCourseInfo(filterCourse, page, perPage, authentication));
  }

  @GetMapping("/info/{id}")
  public ResponseEntity<ResponseCourseInfo> getInfoCourse(Authentication authentication, @PathVariable UUID id) {
    return ResponseEntity.ok(courseService.getCourseInfo(authentication, id));
  }

}

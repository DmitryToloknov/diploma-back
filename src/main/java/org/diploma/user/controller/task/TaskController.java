package org.diploma.user.controller.task;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.diploma.user.Util.TaskLanguage;
import org.diploma.user.controller.task.dto.Creator;
import org.diploma.user.controller.task.dto.RequestTask;
import org.diploma.user.controller.task.dto.ResponseTask;
import org.diploma.user.controller.task.dto.ResponseTaskInfo;
import org.diploma.user.controller.task.dto.TaskFilter;
import org.diploma.user.controller.task.dto.TaskInfo;
import org.diploma.user.controller.task.dto.TaskLanguageInfo;
import org.diploma.user.service.QueueTaskService;
import org.diploma.user.service.TaskService;
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
@RequestMapping("/task")
public class TaskController {

  private final TaskService taskService;
  private final QueueTaskService queueTaskService;

  @PostMapping
  @PreAuthorize("hasRole('COURSE_UPDATE') || hasRole('ADMIN')")
  public ResponseEntity<Long> createTask(Authentication authentication) {
    return ResponseEntity.ok(taskService.createTask(UUID.fromString(authentication.getName())));
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('COURSE_UPDATE') || hasRole('ADMIN')")
  public ResponseEntity deleteTask(@PathVariable Long id) {
    taskService.deleteTask(id);
    return ResponseEntity.ok().build();
  }

  @GetMapping("/{id}")
  public ResponseEntity<ResponseTask> getTask(@PathVariable Long id) {
    return ResponseEntity.ok(taskService.getById(id));
  }

  @PutMapping
  @PreAuthorize("hasRole('COURSE_UPDATE') || hasRole('ADMIN')")
  public ResponseEntity updateTask(@Valid @RequestBody RequestTask requestTask) {
    taskService.updateTask(requestTask);
    return ResponseEntity.ok().build();
  }

  @PostMapping("/{id}/start")
  @PreAuthorize("hasRole('COURSE_UPDATE') || hasRole('ADMIN')")
  public ResponseEntity startTask(@PathVariable Long id) {
    queueTaskService.createQueueTask(id);
    return ResponseEntity.ok().build();
  }

  @GetMapping("/creator")
  @PreAuthorize("hasRole('COURSE_UPDATE') || hasRole('ADMIN')")
  public ResponseEntity<List<Creator>> findAllCreators() {
    return ResponseEntity.ok(taskService.findAllCreator());
  }

  @PostMapping("/total")
  @PreAuthorize("hasRole('COURSE_UPDATE') || hasRole('ADMIN')")
  public ResponseEntity<Long> getNumberTask(@RequestBody TaskFilter taskFilter) {
    return ResponseEntity.ok(taskService.getNumberPage(taskFilter));
  }

  @PostMapping("/{page}/{perPage}")
  @PreAuthorize("hasRole('COURSE_UPDATE') || hasRole('ADMIN')")
  public ResponseEntity<List<ResponseTaskInfo>> getTasks(@RequestBody TaskFilter taskFilter,
                                                         @PathVariable Long page,
                                                         @PathVariable Long perPage) {
    return ResponseEntity.ok(taskService.getTasksInfo(taskFilter, page, perPage));
  }

  @GetMapping("/language")
  public ResponseEntity<List<TaskLanguageInfo>> getFullLanguage() {
    return ResponseEntity.ok(TaskLanguage.getFullLanguage());
  }

  @GetMapping("/{id}/{courseId}")
  public ResponseEntity<TaskInfo> getTask(@PathVariable Long id, @PathVariable UUID courseId, Authentication authentication) {
    return ResponseEntity.ok(taskService.getTask(id, courseId, authentication));
  }

}

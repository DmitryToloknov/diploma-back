package org.diploma.user.service;

import lombok.RequiredArgsConstructor;
import org.diploma.user.Entity.Attempt;
import org.diploma.user.Entity.Task;
import org.diploma.user.Util.CheckAssesCourse;
import org.diploma.user.controller.attempt.dto.AttemptInfo;
import org.diploma.user.controller.attempt.dto.AttemptRequest;
import org.diploma.user.controller.attempt.dto.ReasonApproveAttempt;
import org.diploma.user.controller.attempt.dto.ReasonRejectAttempt;
import org.diploma.user.controller.attempt.dto.ResponseAttempt;
import org.diploma.user.controller.attempt.dto.ResponseTaskInfoForCourse;
import org.diploma.user.controller.attempt.dto.TaskInfoCourse;
import org.diploma.user.exception.CustomException;
import org.diploma.user.repository.AttemptRepository;
import org.diploma.user.repository.CourseRepository;
import org.diploma.user.repository.TaskRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.diploma.user.Util.TaskStatus.ERROR;
import static org.diploma.user.Util.TaskStatus.IN_PROGRESS;
import static org.diploma.user.Util.TaskStatus.NEW;
import static org.diploma.user.Util.TaskStatus.REJECTED;
import static org.diploma.user.Util.TaskStatus.REVIEW;
import static org.diploma.user.Util.TaskStatus.SUCCESS;
import static org.diploma.user.exception.ExceptionMessageConstants.ATTEMPT_APPROVE_ESTIMATION_NOT_VALID;
import static org.diploma.user.exception.ExceptionMessageConstants.ATTEMPT_EXIST;
import static org.diploma.user.exception.ExceptionMessageConstants.ATTEMPT_NOT_FOUND;
import static org.diploma.user.exception.ExceptionMessageConstants.ATTEMPT_STATUS_NOT_REVIEW;
import static org.diploma.user.exception.ExceptionMessageConstants.COURSE_ID_NOT_VALID;
import static org.diploma.user.exception.ExceptionMessageConstants.COURSE_NOT_FOUND;
import static org.diploma.user.exception.ExceptionMessageConstants.REASON_NOT_VALID2;
import static org.diploma.user.exception.ExceptionMessageConstants.TASK_ID_NOT_VALID;
import static org.diploma.user.exception.ExceptionMessageConstants.TASK_NOT_ACCESS;
import static org.diploma.user.exception.ExceptionMessageConstants.TASK_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class AttemptService {

  private final AttemptRepository attemptRepository;
  private final TaskRepository taskRepository;
  private final UserService userService;
  private final CourseService courseService;
  private final CourseRepository courseRepository;

  public void createAttempt(Long taskId, UUID courseId, AttemptRequest attemptRequest, Authentication authentication) {
    if (taskId == null) {
      throw new CustomException(TASK_ID_NOT_VALID);
    }

    if (courseId == null) {
      throw new CustomException(COURSE_ID_NOT_VALID);
    }

    var task = taskRepository.findByIdAndIsDeletedFalse(taskId).orElseThrow(() -> new CustomException(TASK_NOT_FOUND));
    var group = userService.findUserOrThrow(UUID.fromString(authentication.getName())).getGroup();
    CheckAssesCourse.checkAccess(authentication, task, group, courseId);

    var attempts = attemptRepository.findAttempt(taskId, courseId, List.of(REVIEW, NEW, IN_PROGRESS,
        SUCCESS), UUID.fromString(authentication.getName()));

    if (!attempts.isEmpty()) {
      throw new CustomException(ATTEMPT_EXIST);
    }
    var attempt = new Attempt();
    attempt.setAttemptNumber(0);
    attempt.setCourse(courseService.findById(courseId));
    attempt.setUser(userService.findUserOrThrow(UUID.fromString(authentication.getName())));
    attempt.setTaskLanguage(attemptRequest.getLanguage());
    attempt.setCode(attemptRequest.getCode());
    attempt.setStatus(NEW);
    attempt.setTask(task);
    attemptRepository.save(attempt);
  }

  public List<ResponseAttempt> searchAttempt(Long taskId, UUID courseId, Authentication authentication) {
    if (taskId == null) {
      throw new CustomException(TASK_ID_NOT_VALID);
    }

    if (courseId == null) {
      throw new CustomException(COURSE_ID_NOT_VALID);
    }

    var task = taskRepository.findByIdAndIsDeletedFalse(taskId).orElseThrow(() -> new CustomException(TASK_NOT_FOUND));
    var group = userService.findUserOrThrow(UUID.fromString(authentication.getName())).getGroup();
    CheckAssesCourse.checkAccess(authentication, task, group, courseId);

    var attempts = attemptRepository.findAttempt(taskId, courseId,
        List.of(REVIEW, NEW, IN_PROGRESS, SUCCESS, ERROR, REJECTED), UUID.fromString(authentication.getName()));
    return attempts.stream()
        .sorted(Comparator.comparing(Attempt::getUpdatedDateTime).reversed())
        .map(attempt -> new ResponseAttempt(attempt.getId(),
            attempt.getUpdatedDateTime(),
            attempt.getStatus().getDescription(), attempt.getStatus().name())).toList();
  }

  public List<ResponseAttempt> searchAttempt(Long taskId, UUID courseId, UUID userId) {
    if (taskId == null) {
      throw new CustomException(TASK_ID_NOT_VALID);
    }

    if (courseId == null) {
      throw new CustomException(COURSE_ID_NOT_VALID);
    }

    var attempts = attemptRepository.findAttempt(taskId, courseId,
        List.of(REVIEW, NEW, IN_PROGRESS, SUCCESS, ERROR, REJECTED), userId);
    return attempts.stream()
        .sorted(Comparator.comparing(Attempt::getUpdatedDateTime).reversed())
        .map(attempt -> new ResponseAttempt(attempt.getId(),
            attempt.getUpdatedDateTime(),
            attempt.getStatus().getDescription(), attempt.getStatus().name())).toList();
  }

  public void save(Attempt attempt) {
    attemptRepository.save(attempt);
  }

  @Transactional
  public Attempt findFirst() {
    var firstId = attemptRepository.findFirst(NEW);
    return attemptRepository.findAttemptById(firstId);
  }

  public void updateAttemptNumber(Attempt attempt) {
    var attemptNumber = attempt.getAttemptNumber();
    if (attemptNumber == null) {
      attempt.setAttemptNumber(1);
    } else {
      attempt.setAttemptNumber(attemptNumber + 1);
    }
    save(attempt);
  }

  public AttemptInfo findHistory(UUID id) {
    var attempt = attemptRepository.findById(id).orElseThrow(() -> new CustomException(ATTEMPT_NOT_FOUND));

    var attemptInfo = new AttemptInfo();
    attemptInfo.setCode(attempt.getCode());
    attemptInfo.setStatus(attempt.getStatus().getDescription());
    attemptInfo.setStatusKey(attempt.getStatus().name());
    attemptInfo.setResult(attempt.getErrorMessage());
    attemptInfo.setLanguage(attempt.getTaskLanguage().getDescription());
    attemptInfo.setUpdatedDateTime(attempt.getUpdatedDateTime());
    attemptInfo.setReasonRejection(attempt.getReasonRejection());
    attemptInfo.setDowngradeReason(attempt.getDowngradeReason());

    if (attempt.getErrorTestCase() != null) {
      attemptInfo.setInCase(attempt.getErrorTestCase().getInCase());
      attemptInfo.setOutCase(attempt.getErrorTestCase().getOutCase());
    }
    return attemptInfo;
  }

  public ResponseTaskInfoForCourse findTaskInfo(UUID courseId, Authentication authentication) {
    boolean hasEditRole = authentication != null &&
        authentication.getAuthorities().stream()
            .anyMatch(a -> a.getAuthority().equals("ROLE_COURSE_UPDATE") || a.getAuthority().equals("ROLE_ADMIN"));

    var course = courseRepository.findAllByIdAndIsDeletedFalse(courseId).orElseThrow(() -> new CustomException(COURSE_NOT_FOUND));
    if (!hasEditRole) {
      var group = userService.findUserOrThrow(UUID.fromString(authentication.getName())).getGroup();
      if (!course.getGroups().contains(group)) {
        throw new CustomException(TASK_NOT_ACCESS);
      }
    }

    var tasks = taskRepository.findTaskByCourse(courseId);

    var totalEstimation = tasks.stream().mapToInt(Task::getEstimation).sum();
    var taskIds = tasks.stream().map(Task::getId).toList();

    var attempts = attemptRepository.findByTaskId(taskIds, courseId, SUCCESS, UUID.fromString(authentication.getName()));
    Map<Long, Attempt> mapAttempts = attempts.stream()
        .collect(Collectors.toMap(
            attempt -> attempt.getTask().getId(),
            Function.identity()
        ));

    var attemptsInReview = attemptRepository.findByTaskId(taskIds, courseId, REVIEW, UUID.fromString(authentication.getName()));
    Map<Long, Attempt> mapAttemptsInReview = attemptsInReview.stream()
        .collect(Collectors.toMap(
            attempt -> attempt.getTask().getId(),
            Function.identity()
        ));

    var tasksInfo = tasks.stream().map(task -> {
          var taskInfo = new TaskInfoCourse();
          taskInfo.setName(task.getName());
          taskInfo.setId(task.getId());
          taskInfo.setEstimation(task.getEstimation());
          if (mapAttempts.containsKey(task.getId())) {
            var attempt = mapAttempts.get(task.getId());
            taskInfo.setEstimationActual(attempt.getEstimation());
            taskInfo.setDone(true);
          } else {
            taskInfo.setDone(false);
            taskInfo.setEstimationActual(0);
          }

          taskInfo.setReview(mapAttemptsInReview.containsKey(task.getId()));
          return taskInfo;
        }).sorted(Comparator.comparing(TaskInfoCourse::getEstimation))
        .toList();


    var actualEstimation = tasksInfo.stream().mapToInt(TaskInfoCourse::getEstimationActual).sum();

    var result = new ResponseTaskInfoForCourse();

    result.setTasks(tasksInfo);
    result.setTotalEstimation(totalEstimation);
    result.setActualEstimation(actualEstimation);

    return result;
  }

  public ResponseTaskInfoForCourse findTaskInfo(UUID courseId, UUID userId) {

    var tasks = taskRepository.findTaskByCourse(courseId);

    var totalEstimation = tasks.stream().mapToInt(Task::getEstimation).sum();
    var taskIds = tasks.stream().map(Task::getId).toList();

    var attempts = attemptRepository.findByTaskId(taskIds, courseId, SUCCESS, userId);
    Map<Long, Attempt> mapAttempts = attempts.stream()
        .collect(Collectors.toMap(
            attempt -> attempt.getTask().getId(),
            Function.identity()
        ));
    var attemptsInReview = attemptRepository.findByTaskId(taskIds, courseId, REVIEW, userId);
    Map<Long, Attempt> mapAttemptsInReview = attemptsInReview.stream()
        .collect(Collectors.toMap(
            attempt -> attempt.getTask().getId(),
            Function.identity()
        ));


    var tasksInfo = tasks.stream().map(task -> {
          var taskInfo = new TaskInfoCourse();
          taskInfo.setName(task.getName());
          taskInfo.setId(task.getId());
          taskInfo.setEstimation(task.getEstimation());
          if (mapAttempts.containsKey(task.getId())) {
            var attempt = mapAttempts.get(task.getId());
            taskInfo.setEstimationActual(attempt.getEstimation());
            taskInfo.setDone(true);
          } else {
            taskInfo.setDone(false);
            taskInfo.setEstimationActual(0);
          }

          taskInfo.setReview(mapAttemptsInReview.containsKey(task.getId()));
          return taskInfo;
        }).sorted(Comparator.comparing(TaskInfoCourse::getEstimation))
        .toList();


    var actualEstimation = tasksInfo.stream().mapToInt(TaskInfoCourse::getEstimationActual).sum();

    var result = new ResponseTaskInfoForCourse();

    result.setTasks(tasksInfo);
    result.setTotalEstimation(totalEstimation);
    result.setActualEstimation(actualEstimation);

    return result;
  }

  public void rejectById(UUID id, ReasonRejectAttempt reasonAttempt) {
    var attempt = attemptRepository.findById(id).orElseThrow(() -> new CustomException(ATTEMPT_NOT_FOUND));
    if(attempt.getStatus() != REVIEW) {
      throw  new CustomException(ATTEMPT_STATUS_NOT_REVIEW);
    }
    attempt.setReasonRejection(reasonAttempt.getReason());
    attempt.setStatus(REJECTED);
    attemptRepository.save(attempt);
  }

  public void approveById(UUID id, ReasonApproveAttempt reasonApproveAttempt) {
    var attempt = attemptRepository.findById(id).orElseThrow(() -> new CustomException(ATTEMPT_NOT_FOUND));
    var estimation = attempt.getTask().getEstimation();
    if(attempt.getStatus() != REVIEW) {
      throw  new CustomException(ATTEMPT_STATUS_NOT_REVIEW);
    }
    if (estimation < reasonApproveAttempt.getEstimation() || reasonApproveAttempt.getEstimation() < 0) {
      throw new CustomException(String.format(ATTEMPT_APPROVE_ESTIMATION_NOT_VALID, estimation));
    }

    if (estimation != reasonApproveAttempt.getEstimation()) {
      if (reasonApproveAttempt.getReason() == null || reasonApproveAttempt.getReason().isEmpty()) {
        throw new CustomException(REASON_NOT_VALID2);
      }
      attempt.setDowngradeReason(reasonApproveAttempt.getReason());
    }
    attempt.setEstimation(reasonApproveAttempt.getEstimation());
    attempt.setStatus(SUCCESS);

    attemptRepository.save(attempt);
  }

}

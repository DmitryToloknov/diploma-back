package org.diploma.user.service;

import lombok.RequiredArgsConstructor;
import org.diploma.user.Entity.Course;
import org.diploma.user.Entity.Task;
import org.diploma.user.Predicate.CoursePredicate;
import org.diploma.user.controller.course.dto.CourseInfo;
import org.diploma.user.controller.course.dto.CourseProgress;
import org.diploma.user.controller.course.dto.FilterCourse;
import org.diploma.user.controller.course.dto.ResponseCourse;
import org.diploma.user.controller.course.dto.ResponseCourseInfo;
import org.diploma.user.controller.task.dto.ResponseTaskInfo;
import org.diploma.user.exception.CustomException;
import org.diploma.user.mapper.CourseMapper;
import org.diploma.user.repository.AttemptRepository;
import org.diploma.user.repository.CourseRepository;
import org.diploma.user.repository.CourseRepositoryCustom;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.diploma.user.exception.ExceptionMessageConstants.COURSE_NOT_FOUND;
import static org.diploma.user.exception.ExceptionMessageConstants.TASK_EXIST_IN_COURSE;
import static org.diploma.user.exception.ExceptionMessageConstants.TASK_NOT_ACCESS;

@Service
@RequiredArgsConstructor
public class CourseService {

  private final UserService userService;
  private final CourseRepository courseRepository;
  private final CourseMapper courseMapper;
  private final GroupService groupService;
  private final TaskService taskService;
  private final CoursePredicate coursePredicate;
  private final CourseRepositoryCustom courseRepositoryCustom;
  private final AttemptRepository attemptRepository;

  public UUID createCourse(UUID creatorId) {
    var course = new Course();
    course.setName("Новый курс");
    course.setDescription("Описание курса");
    var user = userService.findUserOrThrow(creatorId);
    course.setCreator(user);

    courseRepository.save(course);
    return course.getId();
  }

  public CourseInfo getCourseInfo(UUID id) {
    var course = courseRepository.findAllByIdAndIsDeletedFalse(id).orElseThrow(() -> new CustomException(COURSE_NOT_FOUND));
    return courseMapper.map(course);
  }

  public void deleteCourse(UUID id) {
    var course = courseRepository.findAllByIdAndIsDeletedFalse(id).orElseThrow(() -> new CustomException(COURSE_NOT_FOUND));
    course.setDeleted(true);
    courseRepository.save(course);
  }

  public void updateCourse(CourseInfo courseInfo) {
    var course = courseRepository.findAllByIdAndIsDeletedFalse(courseInfo.getId()).orElseThrow(() -> new CustomException(COURSE_NOT_FOUND));
    course.setName(courseInfo.getName());
    course.setDescription(courseInfo.getDescription());
    course.setLanguages(courseInfo.getLanguages());
    course.setGroups(groupService.findById(courseInfo.getGroups()));
    courseRepository.save(course);
  }

  public void addTask(UUID id, Long taskId) {
    var course = courseRepository.findAllByIdAndIsDeletedFalse(id).orElseThrow(() -> new CustomException(COURSE_NOT_FOUND));
    var task = taskService.findById(taskId);
    var tasks = course.getTasks();
    if (tasks.contains(task)) {
      throw new CustomException(TASK_EXIST_IN_COURSE);
    }
    tasks.add(task);
    course.setTasks(tasks);
    courseRepository.save(course);
  }

  public void removeTask(UUID id, Long taskId) {
    var course = courseRepository.findAllByIdAndIsDeletedFalse(id).orElseThrow(() -> new CustomException(COURSE_NOT_FOUND));
    var task = taskService.findById(taskId);
    var tasks = course.getTasks();
    tasks.remove(task);
    course.setTasks(tasks);
    courseRepository.save(course);
  }

  public List<ResponseTaskInfo> getTasksInfo(UUID id) {
    var course = courseRepository.findAllByIdAndIsDeletedFalse(id).orElseThrow(() -> new CustomException(COURSE_NOT_FOUND));
    var tasks = course.getTasks();
    return taskService.getTasksInfo(tasks);
  }

  public Course findById(UUID id) {
    return courseRepository.findAllByIdAndIsDeletedFalse(id).orElseThrow(() -> new CustomException(COURSE_NOT_FOUND));
  }

  public long getCourseNumber(FilterCourse filterCourse, Authentication authentication) {
    boolean hasEditRole = authentication != null &&
        authentication.getAuthorities().stream()
            .anyMatch(a -> a.getAuthority().equals("ROLE_COURSE_UPDATE") || a.getAuthority().equals("ROLE_ADMIN"));

    var group = userService.findUserOrThrow(UUID.fromString(authentication.getName())).getGroup();
    var predicate = coursePredicate.buildPredicate(filterCourse, hasEditRole, group);
    return courseRepository.count(predicate);
  }

  public List<ResponseCourse> getCourseInfo(FilterCourse filterCourse, long page, long perPage, Authentication authentication) {
    boolean hasEditRole = authentication != null &&
        authentication.getAuthorities().stream()
            .anyMatch(a -> a.getAuthority().equals("ROLE_COURSE_UPDATE") || a.getAuthority().equals("ROLE_ADMIN"));

    var group = userService.findUserOrThrow(UUID.fromString(authentication.getName())).getGroup();
    var predicate = coursePredicate.buildPredicate(filterCourse, hasEditRole, group);

    var ids = courseRepositoryCustom.findAllIds(predicate, page - 1, perPage);
    var courses = courseRepository.findByIdIn(ids);
    return courseMapper.mapResponseCourse(courses);
  }

  public ResponseCourseInfo getCourseInfo(Authentication authentication, UUID id) {
    boolean hasEditRole = authentication != null &&
        authentication.getAuthorities().stream()
            .anyMatch(a -> a.getAuthority().equals("ROLE_COURSE_UPDATE") || a.getAuthority().equals("ROLE_ADMIN"));

    var course = courseRepository.findAllByIdAndIsDeletedFalse(id).orElseThrow(() -> new CustomException(COURSE_NOT_FOUND));
    if (!hasEditRole) {
      var group = userService.findUserOrThrow(UUID.fromString(authentication.getName())).getGroup();
      if(!course.getGroups().contains(group)) {
        throw new CustomException(TASK_NOT_ACCESS);
      }
    }
    return courseMapper.mapResponseCourseInfo(course);
  }

  public List<CourseProgress> getProgressCourse(UUID userId) {
    var attempts = attemptRepository.findAttemptByUserId(userId);
    var courseIds = attempts.stream().map(attempt -> attempt.getCourse().getId()).toList();
    var user = userService.findUserOrThrow(userId);
    var courses = courseRepository.findCourseByIdAndGroupId(courseIds, user.getGroup().getId());

    Map<UUID, Integer> estimationSumByCourse = attempts.stream()
        .collect(Collectors.groupingBy(
            attempt -> attempt.getCourse().getId(),
            Collectors.summingInt(attempt -> attempt.getEstimation() != null ? attempt.getEstimation() : 0)
        ));
    List<CourseProgress> courseProgresses = new ArrayList<>();
    courses.forEach(course -> {
      var courseProgress = new CourseProgress();
      courseProgress.setId(course.getId());
      courseProgress.setName(course.getName());
      courseProgress.setEstimationActual(estimationSumByCourse.getOrDefault(course.getId(), 0));
      var estimation = course.getTasks().stream().mapToInt(Task::getEstimation).sum();
      courseProgress.setEstimation(estimation);
      courseProgresses.add(courseProgress);
    });
    return courseProgresses;
  }

}

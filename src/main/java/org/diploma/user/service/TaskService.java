package org.diploma.user.service;

import lombok.RequiredArgsConstructor;
import org.diploma.user.Entity.Task;
import org.diploma.user.Predicate.TaskPredicate;
import org.diploma.user.Util.CheckAssesCourse;
import org.diploma.user.Util.TaskLanguage;
import org.diploma.user.controller.task.dto.Creator;
import org.diploma.user.controller.task.dto.RequestTask;
import org.diploma.user.controller.task.dto.ResponseTask;
import org.diploma.user.controller.task.dto.ResponseTaskInfo;
import org.diploma.user.controller.task.dto.TaskFilter;
import org.diploma.user.controller.task.dto.TaskInfo;
import org.diploma.user.exception.CustomException;
import org.diploma.user.mapper.TaskMapper;
import org.diploma.user.repository.TaskRepository;
import org.diploma.user.repository.TaskRepositoryCustom;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.diploma.user.exception.ExceptionMessageConstants.TASK_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class TaskService {

  private final UserService userService;
  private final TaskRepository taskRepository;
  private final TaskMapper taskMapper;
  private final SkillService skillService;
  private final TaskPredicate taskPredicate;
  private final TaskRepositoryCustom taskRepositoryCustom;

  public Long createTask(UUID creatorId) {
    var task = new Task();
    task.setCreator(userService.findUserOrThrow(creatorId));
    task.setName("Новая задача");
    task.setDescription("");
    task.setCode("");
    task.setEstimation(0);
    task.setMemoryLimit(0);
    task.setTimeLimit(0);
    task.setTaskLanguage(TaskLanguage.PYTHON);
    taskRepository.save(task);
    return task.getId();
  }

  public void deleteTask(Long taskId) {
    taskRepository.findById(taskId).ifPresent(task -> {
      task.setDeleted(true);
      taskRepository.save(task);
    });
  }

  public Task findById(Long id) {
    return taskRepository.findByIdAndIsDeletedFalse(id).orElseThrow(() -> new CustomException(TASK_NOT_FOUND));
  }

  public ResponseTask getById(Long id) {
    var task = taskRepository.findByIdAndIsDeletedFalse(id).orElseThrow(() -> new CustomException(TASK_NOT_FOUND));
    return taskMapper.map(task);
  }

  public void updateTask(RequestTask requestTask) {
    var task = taskRepository.findByIdAndIsDeletedFalse(requestTask.getId()).orElseThrow(() -> new CustomException(TASK_NOT_FOUND));
    task.setName(requestTask.getName());
    task.setDescription(requestTask.getDescription());
    task.setCode(requestTask.getCode());
    task.setTaskLanguage(requestTask.getTaskLanguage());
    task.setTimeLimit(requestTask.getTimeLimit());
    task.setMemoryLimit(requestTask.getMemoryLimit());
    task.setEstimation(requestTask.getEstimation());
    task.setSkills(skillService.getStallsByIds(requestTask.getSkills()));
    taskRepository.save(task);
  }

  public List<Creator> findAllCreator() {
    var creators = taskRepository.findAllCreators();
    return taskMapper.mapToCreator(creators);
  }

  public long getNumberPage(TaskFilter taskFilter) {
    return taskRepository.count(taskPredicate.buildPredicate(taskFilter));
  }

  public List<ResponseTaskInfo> getTasksInfo(TaskFilter taskFilter, long page, long perPage) {
    var predicate = taskPredicate.buildPredicate(taskFilter);
    var tasks = taskRepository.findByIdIn(taskRepositoryCustom.findAllIds(predicate, page - 1, perPage));
    return taskMapper.mapToResponseTaskInfo(tasks);
  }

  public List<ResponseTaskInfo> getTasksInfo(Set<Task> tasks) {
    return taskMapper.mapToResponseTaskInfo(tasks.stream().toList());
  }

  public TaskInfo getTask(Long id, UUID courseId, Authentication authentication) {
    var task = taskRepository.findByIdAndIsDeletedFalse(id).orElseThrow(() -> new CustomException(TASK_NOT_FOUND));
    var group = userService.findUserOrThrow(UUID.fromString(authentication.getName())).getGroup();
    CheckAssesCourse.checkAccess(authentication, task, group, courseId);
    var first = task.getCourses().stream().filter(course -> courseId.equals(course.getId())).findFirst();
    var course = first.get();
    var languages = course.getLanguages();
    var taskInfo = taskMapper.mapToTaskInfo(task);
    taskInfo.setLanguages(TaskLanguage.getLanguageInfo(languages));
    return taskInfo;
  }

}

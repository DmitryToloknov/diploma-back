package org.diploma.user.mapper;

import org.diploma.user.Entity.Skill;
import org.diploma.user.Entity.Task;
import org.diploma.user.Entity.User;
import org.diploma.user.controller.task.dto.Creator;
import org.diploma.user.controller.task.dto.ResponseTask;
import org.diploma.user.controller.task.dto.ResponseTaskInfo;
import org.diploma.user.controller.task.dto.TaskInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Mapper
public interface TaskMapper {

  @Mapping(source = "creator", target = "creatorName", qualifiedByName = "getUserName")
  ResponseTask map(Task task);

  default List<UUID> map(Set<Skill> skills) {
    if (skills == null) {
      return Collections.emptyList();
    }
    return skills.stream()
        .map(Skill::getId)
        .collect(Collectors.toList());
  }

  @Named("getUserName")
  default String getUserName(User user) {
    return user.getName() + user.getSurname();
  }

  @Mapping(target = "name", expression = "java(user.getName() + \" \" + user.getSurname())")
  Creator map(User user);

  List<Creator> mapToCreator(List<User> users);

  ResponseTaskInfo mapToResponseTaskInfo(Task task);
  List<ResponseTaskInfo> mapToResponseTaskInfo(List<Task> tasks);

  TaskInfo mapToTaskInfo(Task task);

}

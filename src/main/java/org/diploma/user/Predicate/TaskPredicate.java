package org.diploma.user.Predicate;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import org.diploma.user.Entity.QTask;
import org.diploma.user.controller.task.dto.TaskFilter;
import org.springframework.stereotype.Service;

@Service
public class TaskPredicate {

  public Predicate buildPredicate(TaskFilter taskFilter) {
    var predicate = new BooleanBuilder();
    var task = QTask.task;
    if (taskFilter.getName() != null && !taskFilter.getName().isEmpty()) {
      String pattern = "%" + taskFilter.getName().toLowerCase() + "%";
      predicate.and(task.name.lower().like(pattern));
    }


    if (taskFilter.getSkills()!=null && !taskFilter.getSkills().isEmpty()) {
      predicate.and(task.skills.any().id.in(taskFilter.getSkills()));
    }

    if (taskFilter.getCreators()!=null && !taskFilter.getCreators().isEmpty()) {
      predicate.and(task.creator.id.in(taskFilter.getCreators()));
    }

    predicate.and(task.isDeleted.eq(false));

    return predicate;
  }

}

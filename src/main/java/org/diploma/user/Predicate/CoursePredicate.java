package org.diploma.user.Predicate;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import org.diploma.user.Entity.Group;
import org.diploma.user.Entity.QCourse;
import org.diploma.user.controller.course.dto.FilterCourse;
import org.springframework.stereotype.Service;

@Service
public class CoursePredicate {

  public Predicate buildPredicate(FilterCourse filterCourse, boolean isAdmin, Group group) {
    var predicate = new BooleanBuilder();
    var course = QCourse.course;
    if (filterCourse.getName() != null && !filterCourse.getName().isEmpty()) {
      String pattern = "%" + filterCourse.getName().toLowerCase() + "%";
      predicate.and(course.name.lower().like(pattern));
    }

    if(!isAdmin) {
      predicate.and(course.groups.any().id.eq(group.getId()));
    }

    predicate.and(course.isDeleted.eq(false));

    return predicate;
  }

}

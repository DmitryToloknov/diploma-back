package org.diploma.user.Util;

import org.diploma.user.Entity.Group;
import org.diploma.user.Entity.Task;
import org.diploma.user.exception.CustomException;
import org.springframework.security.core.Authentication;

import java.util.UUID;

import static org.diploma.user.exception.ExceptionMessageConstants.TASK_NOT_ACCESS;
import static org.diploma.user.exception.ExceptionMessageConstants.TASK_NOT_IN_COURSE;

public class CheckAssesCourse {

  public static void checkAccess(Authentication authentication, Task task, Group group, UUID courseId) {
    boolean hasEditRole = authentication != null &&
        authentication.getAuthorities().stream()
            .anyMatch(a -> a.getAuthority().equals("ROLE_COURSE_UPDATE") || a.getAuthority().equals("ROLE_ADMIN"));

    var first = task.getCourses().stream().filter(course -> courseId.equals(course.getId())).findFirst();

    if (first.isEmpty()) {
      throw new CustomException(TASK_NOT_IN_COURSE);
    }
    var course = first.get();

    if (!hasEditRole && !course.getGroups().contains(group)) {
      throw new CustomException(TASK_NOT_ACCESS);
    }
  }
}

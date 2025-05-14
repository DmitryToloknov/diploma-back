package org.diploma.user.controller.course.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.diploma.user.Util.TaskLanguage;

import java.util.List;
import java.util.UUID;

import static org.diploma.user.exception.ExceptionMessageConstants.COURSE_DESCRIPTION_NOT_VALID;
import static org.diploma.user.exception.ExceptionMessageConstants.COURSE_LANGUAGE_NOT_VALID;
import static org.diploma.user.exception.ExceptionMessageConstants.COURSE_NAME_NOT_VALID;

@Getter
@Setter
public class CourseInfo {
  private UUID id;
  @NotBlank(message = COURSE_NAME_NOT_VALID)
  private String name;
  @NotBlank(message = COURSE_DESCRIPTION_NOT_VALID)
  private String description;
  @Size(min = 1, message = COURSE_LANGUAGE_NOT_VALID)
  private List<TaskLanguage> languages;
  private List<UUID> groups;
  private String creatorName;
}

package org.diploma.user.controller.task.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import org.diploma.user.Util.TaskLanguage;

import java.util.List;
import java.util.UUID;

import static org.diploma.user.exception.ExceptionMessageConstants.TASK_CODE_NOT_VALID;
import static org.diploma.user.exception.ExceptionMessageConstants.TASK_DESCRIPTION_NOT_VALID;
import static org.diploma.user.exception.ExceptionMessageConstants.TASK_ESTIMATION_NOT_VALID;
import static org.diploma.user.exception.ExceptionMessageConstants.TASK_ESTIMATION_NOT_VALID_POSITIVE;
import static org.diploma.user.exception.ExceptionMessageConstants.TASK_LANGUAGE_NOT_VALID;
import static org.diploma.user.exception.ExceptionMessageConstants.TASK_MEMORY_LIMIT_NOT_VALID;
import static org.diploma.user.exception.ExceptionMessageConstants.TASK_NAME_NOT_VALID;
import static org.diploma.user.exception.ExceptionMessageConstants.TASK_TIME_LIMIT_NOT_VALID;

@Getter
@Setter
public class RequestTask {
  private Long id;
  @NotBlank(message = TASK_NAME_NOT_VALID)
  private String name;
  @NotBlank(message = TASK_DESCRIPTION_NOT_VALID)
  private String description;
  @NotBlank(message = TASK_CODE_NOT_VALID)
  private String code;
  @NotNull(message = TASK_ESTIMATION_NOT_VALID)
  @Positive(message = TASK_ESTIMATION_NOT_VALID_POSITIVE)
  private Integer estimation;
  @NotNull(message = TASK_TIME_LIMIT_NOT_VALID)
  @Positive(message = TASK_TIME_LIMIT_NOT_VALID)
  private Integer timeLimit;
  @NotNull(message = TASK_MEMORY_LIMIT_NOT_VALID)
  @Positive(message = TASK_MEMORY_LIMIT_NOT_VALID)
  private Integer memoryLimit;
  @NotNull(message = TASK_LANGUAGE_NOT_VALID)
  private TaskLanguage taskLanguage;
  private List<UUID> skills;
}

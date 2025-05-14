package org.diploma.user.controller.attempt.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.diploma.user.Util.TaskLanguage;

import static org.diploma.user.exception.ExceptionMessageConstants.TASK_CODE_NOT_VALID;
import static org.diploma.user.exception.ExceptionMessageConstants.TASK_LANGUAGE_NOT_VALID;

@Getter
@Setter
public class AttemptRequest {
  @NotBlank(message = TASK_CODE_NOT_VALID)
  private String code;
  @NotNull(message = TASK_LANGUAGE_NOT_VALID)
  private TaskLanguage language;
}

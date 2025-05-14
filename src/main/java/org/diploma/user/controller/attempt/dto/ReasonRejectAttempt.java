package org.diploma.user.controller.attempt.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import static org.diploma.user.exception.ExceptionMessageConstants.REASON_NOT_VALID;

@Getter
@Setter
public class ReasonRejectAttempt {
  @NotBlank(message = REASON_NOT_VALID)
  private String reason;
}

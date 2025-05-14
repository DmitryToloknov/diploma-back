package org.diploma.user.controller.user.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import static org.diploma.user.exception.ExceptionMessageConstants.USER_DELETION_REASON_NOT_VALID;

@Getter
@Setter
public class UserDeleteRequest {

  @Pattern(regexp = "^.{5,100}$", message = USER_DELETION_REASON_NOT_VALID)
  private String deletionReason;
}

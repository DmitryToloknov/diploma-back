package org.diploma.user.controller.auth.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import static org.diploma.user.exception.ExceptionMessageConstants.PASSWORD_PATTERN_NOT_VALID;

@Setter
@Getter
public class UpdatePasswordRequest {

  @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-+]).{8,}$", message = PASSWORD_PATTERN_NOT_VALID)
  private String password;

}

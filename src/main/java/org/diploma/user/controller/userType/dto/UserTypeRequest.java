package org.diploma.user.controller.userType.dto;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static org.diploma.user.exception.ExceptionMessageConstants.USER_TYPE_NOT_VALID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserTypeRequest {
  @Pattern(regexp = "^[а-яА-ЯёЁa-zA-Z\s]{2,100}$", message = USER_TYPE_NOT_VALID)
  private String name;
}

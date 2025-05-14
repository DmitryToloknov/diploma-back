package org.diploma.user.controller.auth.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

import static org.diploma.user.exception.ExceptionMessageConstants.LOGIN_PATTERN_NOT_VALID;
import static org.diploma.user.exception.ExceptionMessageConstants.NAME_PATTERN_NOT_VALID;
import static org.diploma.user.exception.ExceptionMessageConstants.PASSWORD_PATTERN_NOT_VALID;
import static org.diploma.user.exception.ExceptionMessageConstants.PHONE_PATTERN_NOT_VALID;
import static org.diploma.user.exception.ExceptionMessageConstants.SURNAME_PATTERN_NOT_VALID;

@Getter
@Setter
public class RegisterRequest {

  @Pattern(regexp = "^[а-яА-ЯёЁa-zA-Z]{2,100}$", message = NAME_PATTERN_NOT_VALID)
  private String name;
  @Pattern(regexp = "^[а-яА-ЯёЁa-zA-Z]{2,100}$", message = SURNAME_PATTERN_NOT_VALID)
  private String surname;
  @Pattern(regexp = "^[a-zA-Z][a-zA-Z0-9-_\\.]{2,200}$", message = LOGIN_PATTERN_NOT_VALID)
  private String login;
  @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-+]).{8,}$", message = PASSWORD_PATTERN_NOT_VALID)
  private String password;
  @Pattern(regexp = "^((\\+7)+([0-9]){10})$", message = PHONE_PATTERN_NOT_VALID)
  private String phone;
  private List<UUID> type;
  private UUID groupId;

}

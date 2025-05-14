package org.diploma.user.controller.user.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

import static org.diploma.user.exception.ExceptionMessageConstants.NAME_PATTERN_NOT_VALID;
import static org.diploma.user.exception.ExceptionMessageConstants.PHONE_PATTERN_NOT_VALID;
import static org.diploma.user.exception.ExceptionMessageConstants.SURNAME_PATTERN_NOT_VALID;

@Getter
@Setter
public class UpdateUserRequest {

  @Pattern(regexp = "^[а-яА-ЯёЁa-zA-Z]{2,100}$", message = NAME_PATTERN_NOT_VALID)
  private String name;
  @Pattern(regexp = "^[а-яА-ЯёЁa-zA-Z]{2,100}$", message = SURNAME_PATTERN_NOT_VALID)
  private String surname;
  @Pattern(regexp = "^((\\+7)+([0-9]){10})$", message = PHONE_PATTERN_NOT_VALID)
  private String phone;
  private List<UUID> types;
  private UUID groupId;

}

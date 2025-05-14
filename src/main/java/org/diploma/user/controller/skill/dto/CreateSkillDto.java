package org.diploma.user.controller.skill.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import static org.diploma.user.exception.ExceptionMessageConstants.SKILL_NAME_NOT_VALID;

@Getter
@Setter
public class CreateSkillDto {

  @Pattern(regexp = "^.{5,100}$", message = SKILL_NAME_NOT_VALID)
  private String name;
}

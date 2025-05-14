package org.diploma.user.controller.group.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Year;
import java.util.UUID;

import static org.diploma.user.exception.ExceptionMessageConstants.GROUP_COUNT_COURSE_NOT_VALID;
import static org.diploma.user.exception.ExceptionMessageConstants.GROUP_NAME_NOT_VALID;
import static org.diploma.user.exception.ExceptionMessageConstants.GROUP_SHORT_NAME_NOT_VALID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GroupRequest {

  @Pattern(regexp = "^[а-яА-ЯёЁa-zA-Z\s]{2,100}$", message = GROUP_NAME_NOT_VALID)
  private String name;
  @Pattern(regexp = "^[а-яА-ЯёЁa-zA-Z0-9\s]{2,15}$", message = GROUP_SHORT_NAME_NOT_VALID)
  private String shortName;
  @NotNull
  private Year year;
  @Positive(message = GROUP_COUNT_COURSE_NOT_VALID)
  @Max(value = 8, message = GROUP_COUNT_COURSE_NOT_VALID)
  private Integer countCourse;
  private UUID curatorId;

}

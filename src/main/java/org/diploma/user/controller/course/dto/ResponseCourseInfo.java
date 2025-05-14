package org.diploma.user.controller.course.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ResponseCourseInfo {
  private UUID id;
  private String name;
  private String description;
}

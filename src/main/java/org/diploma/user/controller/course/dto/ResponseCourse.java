package org.diploma.user.controller.course.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
public class ResponseCourse {
  private UUID id;
  private String name;
  private String creatorName;
}

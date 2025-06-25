package org.diploma.user.controller.course.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CourseProgress {
  private UUID id;
  private String name;
  private Integer estimationActual;
  private Integer estimation;
}

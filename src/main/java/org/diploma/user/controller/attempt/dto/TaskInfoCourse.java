package org.diploma.user.controller.attempt.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskInfoCourse {

  private Long id;
  private String name;
  private boolean isDone;
  private int estimation;
  private int estimationActual;
  private boolean isReview;

}

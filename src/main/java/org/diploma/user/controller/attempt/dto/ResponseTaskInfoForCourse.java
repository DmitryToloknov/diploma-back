package org.diploma.user.controller.attempt.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ResponseTaskInfoForCourse {

  private int totalEstimation;
  private int actualEstimation;
  private List<TaskInfoCourse> tasks;

}

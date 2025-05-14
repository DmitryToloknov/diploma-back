package org.diploma.user.controller.task.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseTaskInfo {

  private Long id;

  private String name;

  private int estimation;
}

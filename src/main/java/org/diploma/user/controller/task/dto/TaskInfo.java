package org.diploma.user.controller.task.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TaskInfo {
  private String name;
  private String description;
  private List<TaskLanguageInfo> languages;
}

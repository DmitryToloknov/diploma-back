package org.diploma.user.controller.task.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class ResponseTask {
  private Long id;
  private String name;
  private String description;
  private String code;
  private Integer estimation;
  private String creatorName;
  private Integer timeLimit;
  private Integer memoryLimit;
  private String taskLanguage;
  private List<UUID> skills;
}

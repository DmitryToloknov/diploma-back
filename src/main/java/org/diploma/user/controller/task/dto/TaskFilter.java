package org.diploma.user.controller.task.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class TaskFilter {
  private String name;
  private List<UUID> skills;
  private List<UUID> creators;
}

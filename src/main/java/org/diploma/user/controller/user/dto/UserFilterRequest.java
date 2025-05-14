package org.diploma.user.controller.user.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class UserFilterRequest {
  private List<UUID> userTypeIds;
  private List<UUID> groupIds;
  private String name;
  private Boolean onlyActiveUser;
}

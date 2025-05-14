package org.diploma.user.controller.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RoleResponseForUser {
  private String name;
  private String description;
}

package org.diploma.user.controller.skill.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ResponseSkill {
  private UUID id;
  private String name;
}

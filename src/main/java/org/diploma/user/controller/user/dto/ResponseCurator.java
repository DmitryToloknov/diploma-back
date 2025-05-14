package org.diploma.user.controller.user.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ResponseCurator {
  private UUID id;
  private String userName;
}

package org.diploma.user.controller.testcase.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ResponseTestCase {
  private UUID id;
  private String inCase;
  private String OutCase;
}

package org.diploma.user.controller.attempt.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AttemptInfo {
  private String code;
  private String inCase;
  private String outCase;
  private String result;
  private LocalDateTime updatedDateTime;
  private String status;
  private String statusKey;
  private String language;
  private String reasonRejection;
  private String downgradeReason;
}

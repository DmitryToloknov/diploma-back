package org.diploma.user.controller.attempt.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class ResponseAttempt {
  private UUID id;
  private LocalDateTime createdDateTime;
  private String status;
  private String statusKey;
}

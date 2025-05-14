package org.diploma.user.controller.userType.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserTypeInfoResponse {

  private UUID id;
  private String name;
  private UUID creatorId;
  private LocalDateTime createDateTime;
  private LocalDateTime updateDateTime;

}

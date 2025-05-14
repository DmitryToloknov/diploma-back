package org.diploma.user.controller.user.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class ResponseUser {
  private UUID id;
  private String login;
  private String name;
  private String surname;
  private String phone;
  private UUID groupId;
  private String groupName;
  private boolean status;
  private String deletionReason;
  private List<UUID> types;
}

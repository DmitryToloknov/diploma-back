package org.diploma.user.Util;

import lombok.Getter;
import org.diploma.user.controller.user.dto.RoleResponseForUser;

import java.util.ArrayList;
import java.util.List;

@Getter
public enum Role {
  ROLE_GROUP_UPDATE ("Управление группами"),
  ROLE_USER_UPDATE ("Управление пользователями"),
  ROLE_ADMIN ("Права администратора"),
  ROLE_NEWS_UPDATE ("Управление новостями"),
  ROLE_COURSE_UPDATE ("Управление курсами"),
  ROLE_SCHEDULE_UPDATE ("Управление расписанием");

  private final String description;
  Role(String description) {
    this.description = description;
  }

  public static List<RoleResponseForUser> getFullRole() {
    List<RoleResponseForUser> responses = new ArrayList<>();
    for (Role role : Role.values()) {
      responses.add(new RoleResponseForUser(role.name(), role.getDescription()));
    }
    return responses;
  }

  public static List<String> getNameRoles() {
    List<String> responses = new ArrayList<>();
    for (Role role : Role.values()) {
      responses.add(role.name());
    }
    return responses;
  }

}

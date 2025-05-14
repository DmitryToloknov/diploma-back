package org.diploma.user.Util;

import lombok.Getter;

@Getter
public enum TaskStatus {
  SUCCESS("Выполнено успешно"),
  NEW("Ожидает начала проверки"),
  IN_PROGRESS("Проверка задания системой"),
  ERROR("Обнаружены ошибки при проверке"),
  REJECTED("Преподаватель отклонил решение"),
  REVIEW("Ожидает проверки преподавателем");

  private final String description;

  TaskStatus(String description) {
    this.description = description;
  }
}

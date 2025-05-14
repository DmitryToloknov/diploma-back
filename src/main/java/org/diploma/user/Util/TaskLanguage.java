package org.diploma.user.Util;

import lombok.Getter;
import org.diploma.user.controller.task.dto.TaskLanguageInfo;

import java.util.ArrayList;
import java.util.List;

@Getter
public enum TaskLanguage {
  CPP("cpp"),
  PYTHON("python");

  private final String description;

  TaskLanguage(String description) {
    this.description = description;
  }

  public static List<TaskLanguageInfo> getFullLanguage() {
    List<TaskLanguageInfo> responses = new ArrayList<>();
    for (TaskLanguage language : TaskLanguage.values()) {
      responses.add(new TaskLanguageInfo(language.name(), language.getDescription()));
    }
    return responses;
  }

  public static List<TaskLanguageInfo> getLanguageInfo(List<TaskLanguage> languages) {
    List<TaskLanguageInfo> responses = new ArrayList<>();
    for (TaskLanguage language : languages) {
      responses.add(new TaskLanguageInfo(language.name(), language.getDescription()));
    }
    return responses;
  }
}

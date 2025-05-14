package org.diploma.user.controller.news.dto;

import lombok.Getter;
import lombok.Setter;
import org.diploma.user.Entity.News;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for {@link News}
 */

@Getter
@Setter
public class NewsResponse {
  private UUID id;
  private String name;
  private String description;
  private boolean isStatus;
  private LocalDateTime createdDateTime;
  private LocalDateTime datePublication;
  private LocalDateTime updatedDateTime;
  private UUID creatorId;
  private String fullNameCreator;
}
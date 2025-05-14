package org.diploma.user.controller.news.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.diploma.user.Entity.News;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for {@link News}
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NewsPreviewResponse {
  private UUID id;
  private String name;
  private String description;
  private LocalDateTime datePublication;
  private UUID creatorId;
  private String fullNameCreator;
  private boolean isStatus;
}
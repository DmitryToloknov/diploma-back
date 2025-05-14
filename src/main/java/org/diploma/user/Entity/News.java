package org.diploma.user.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "agu_news")
public class News {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(nullable = false, length = 100)
  private String name;

  @Column(columnDefinition = "text")
  private String description;

  private UUID creatorId;

  private boolean isDeleted;

  /**
   * Опубликованна ли.
   */
  private boolean isStatus;


  /**
   * Время создания.
   */
  @CreationTimestamp
  private LocalDateTime createdDateTime;

  /**
   * Заполняется, при первой публикации.
   */
  private LocalDateTime datePublication;

  /**
   * Время обновления.
   */
  @UpdateTimestamp
  private LocalDateTime updatedDateTime;
}

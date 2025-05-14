package org.diploma.user.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.diploma.user.Util.TaskLanguage;
import org.diploma.user.Util.TaskStatus;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "agu_attempts")
public class Attempt {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @ManyToOne(fetch = FetchType.LAZY)
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  private Task task;

  @ManyToOne(fetch = FetchType.LAZY)
  private Course course;

  private Integer attemptNumber;

  private Integer estimation;

  @Column(columnDefinition = "text")
  private String code;

  @Enumerated(EnumType.STRING)
  private TaskStatus status;

  @ManyToOne(fetch = FetchType.LAZY)
  private TestCase errorTestCase;
  @Column(columnDefinition = "text")
  private String errorMessage;

  /**
   * Причина отклонения решения.
   */
  @Column(columnDefinition = "text")
  private String reasonRejection;

  /**
   * Причина снижения оценки.
   */
  @Column(columnDefinition = "text")
  private String downgradeReason;

  @Enumerated(EnumType.STRING)
  private TaskLanguage taskLanguage;

  /**
   * Время создания.
   */
  @CreationTimestamp
  private LocalDateTime createdDateTime;

  @UpdateTimestamp
  private LocalDateTime updatedDateTime;

}

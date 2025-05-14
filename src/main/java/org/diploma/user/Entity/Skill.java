package org.diploma.user.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
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
@Table(name = "agu_skills")
public class Skill {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  /**
   * Название скила.
   */
  private String name;

  private boolean isDeleted;


  @ManyToOne(fetch = FetchType.LAZY)
  private User creator;

  /**
   * Время создания.
   */
  @CreationTimestamp
  private LocalDateTime createdDateTime;

  /**
   * Время обновления.
   */
  @UpdateTimestamp
  private LocalDateTime updatedDateTime;

}

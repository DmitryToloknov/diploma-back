package org.diploma.user.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.time.Year;
import java.util.Set;
import java.util.UUID;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "agu_groups")
public class Group {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  /**
   * Название группы.
   */
  @Column(nullable = false, length = 100)
  private String name;

  /**
   * Короткое название.
   */
  @Column(nullable = false, length = 15)
  private String shortName;

  /**
   * Год создания группы.
   */
  @Column(nullable = false)
  private Year year;

  /**
   * Количество курсов.
   */
  @Column(nullable = false)
  private Integer countCourse;

  /**
   * Статус группы.
   */
  private boolean deleted;

  /**
   * Студенты состоящие в группе.
   */
  @OneToMany(fetch = FetchType.LAZY, mappedBy = "group")
  private Set<User> students;

  /**
   * Куратор группы.
   */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "curator_id")
  private User curator;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "creator_id")
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

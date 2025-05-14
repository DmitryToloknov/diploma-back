package org.diploma.user.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.diploma.user.Util.TaskLanguage;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "agu_tasks")
public class Task {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Long id;

  /**
   * Название задачи.
   */
  private String name;
  /**
   * Описание задачи.
   */
  @Column(columnDefinition = "text")
  private String description;

  private Integer timeLimit;
  private Integer memoryLimit;

  @Column(columnDefinition = "text")
  private String code;

  @Enumerated(EnumType.STRING)
  private TaskLanguage taskLanguage;

  private boolean isDeleted;

  /**
   * Баллов за решение.
   */
  private int estimation;

  @ManyToMany
  @JoinTable(
      joinColumns = { @JoinColumn(name = "task_id") },
      inverseJoinColumns = { @JoinColumn(name = "skill_id") }
  )
  private Set<Skill> skills;

  @ManyToMany
  @JoinTable(
      name = "agu_courses_tasks",
      joinColumns = { @JoinColumn(name = "task_id") },
      inverseJoinColumns = { @JoinColumn(name = "course_id") }
  )
  private Set<Course> courses;

  @OneToMany
  private Set<TestCase> testCases;

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

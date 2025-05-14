package org.diploma.user.Entity;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
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
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.diploma.user.Util.TaskLanguage;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "agu_courses")
public class Course {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  /**
   * Название курса.
   */
  private String name;
  /**
   * Описание курса.
   */
  @Column(columnDefinition = "text")
  private String description;

  private boolean isDeleted;


  @ManyToOne(fetch = FetchType.LAZY)
  private User creator;

  @ManyToMany
  @JoinTable(
      joinColumns = { @JoinColumn(name = "course_id") },
      inverseJoinColumns = { @JoinColumn(name = "group_id") }
  )
  private Set<Group> groups;

  @ManyToMany
  @JoinTable(
      name = "agu_courses_tasks",
      joinColumns = { @JoinColumn(name = "course_id") },
      inverseJoinColumns = { @JoinColumn(name = "task_id") }
  )
  private Set<Task> tasks;

  @Column(name = "language")
  @Enumerated(EnumType.STRING)
  @ElementCollection(targetClass = TaskLanguage.class)
  @CollectionTable(name = "agu_course_languages")
  private List<TaskLanguage> languages;

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

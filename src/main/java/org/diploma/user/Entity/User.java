package org.diploma.user.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "agu_users")
public class User {

  @Id
  private UUID id;

  @Column(nullable = false, length = 100)
  private String name;

  @Column(nullable = false, length = 100)
  private String surname;

  @Column(nullable = false, unique = true, length = 200)
  private String login;

  @Column(nullable = false, length = 12)
  private String phone;

  @ManyToMany
  @JoinTable(
      joinColumns = { @JoinColumn(name = "user_id") },
      inverseJoinColumns = { @JoinColumn(name = "user_type_id") }
  )
  private Set<UserType> types;

  /**
   * В какой группе состоит.
   */
  @ManyToOne(fetch = FetchType.LAZY)
  private Group group;

  /**
   * Статус пользователя.
   */
  private boolean isStatus;

  /**
   * Создатель.
   */

  @ManyToOne(fetch = FetchType.LAZY)
  private User creator;

  /**
   * Причина не активности.
   */
  private String deletionReason;

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

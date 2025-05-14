package org.diploma.user.repository;

import com.querydsl.core.types.Predicate;
import org.diploma.user.Entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID>, QuerydslPredicateExecutor<User> {
  Optional<User> findByLoginAndIsStatusTrue(String login);

  @Query("SELECT u FROM User u JOIN u.types t WHERE t.name = 'Куратор' and u.isStatus = true ")
  List<User> findCurators();

  long count(Predicate predicate);

  @EntityGraph(attributePaths = {"group"})
  List<User> findByIdIn(List<UUID> ids);

  @Query("""
            select u from User u
            where u.group.id =:groupId
            and u.isStatus = true
""")
  List<User> findByGroup(UUID groupId);

}

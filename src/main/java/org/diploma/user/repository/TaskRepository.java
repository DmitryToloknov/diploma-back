package org.diploma.user.repository;

import com.querydsl.core.types.Predicate;
import org.diploma.user.Entity.Task;
import org.diploma.user.Entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskRepository extends CrudRepository<Task, Long>, QuerydslPredicateExecutor<Task> {
  Optional<Task> findByIdAndIsDeletedFalse(Long id);

  @Query("select t.creator from Task t group by t.creator")
  List<User> findAllCreators();

  long count(Predicate predicate);

  List<Task> findByIdIn(List<Long> ids);

  @Query("""
          select t
          from Task t
          join t.courses c
          where c.id = :id
      """)
  List<Task> findTaskByCourse(UUID id);
}

package org.diploma.user.repository;

import com.querydsl.core.types.Predicate;
import org.diploma.user.Entity.Course;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CourseRepository extends CrudRepository<Course, UUID>, QuerydslPredicateExecutor<Course> {

  Optional<Course> findAllByIdAndIsDeletedFalse(UUID id);

  long count(Predicate predicate);

  List<Course> findByIdIn(List<UUID> ids);

}

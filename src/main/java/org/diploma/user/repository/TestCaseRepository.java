package org.diploma.user.repository;

import org.diploma.user.Entity.TestCase;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TestCaseRepository extends CrudRepository<TestCase, UUID> {

  @Query("select t from TestCase t where t.deleted = false and t.task.id =:taskId and t.task.isDeleted = false ")
  List<TestCase> findTestCases(Long taskId);

  Optional<TestCase> findByIdAndDeletedFalse(UUID id);
}

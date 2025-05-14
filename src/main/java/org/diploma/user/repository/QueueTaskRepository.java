package org.diploma.user.repository;

import org.diploma.user.Entity.QueueTask;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface QueueTaskRepository extends CrudRepository<QueueTask, UUID> {

  Optional<QueueTask> findByTaskId(Long taskId);

  @Query("SELECT t from QueueTask t order by t.createdDateTime limit 1")
  QueueTask findFirst();
}

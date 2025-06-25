package org.diploma.user.repository;

import org.diploma.user.Entity.Attempt;
import org.diploma.user.Entity.QueueTask;
import org.diploma.user.Util.TaskStatus;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Repository
public interface AttemptRepository extends CrudRepository<Attempt, UUID> {

  @Query("""
      select a
      from Attempt a
      where a.task.id = :taskId
      and a.course.id = :courseId
      and a.status in :statuses
      and a.user.id =:userId
      """)
  List<Attempt> findAttempt(Long taskId,
                            UUID courseId,
                            List<TaskStatus> statuses,
                            UUID userId);

  @Query("""
      SELECT a.id
      from Attempt a
      where a.status =:status
      and a.attemptNumber < 2
      order by a.createdDateTime
      limit 1
      """)
  UUID findFirst(TaskStatus status);

  @Query("""
      SELECT a
      from Attempt a
             JOIN FETCH a.user
             JOIN FETCH a.course
             JOIN FETCH a.task t
      where a.id =:id
      """)
  Attempt findAttemptById(UUID id);

  @Query("""
            SELECT a
            from Attempt a
            where a.task.id in :taskIds
            and a.course.id =:courseId
            and a.status =:status
            and a.user.id =:userId
      """)
  List<Attempt> findByTaskId(List<Long> taskIds, UUID courseId, TaskStatus status, UUID userId);

  @Query("""
      SELECT a
      from Attempt a
             JOIN FETCH a.course
      where a.user.id =:id
      """)
  List<Attempt> findAttemptByUserId(UUID id);

  @Query("""
      SELECT a
      from Attempt a
             JOIN FETCH a.task
      where a.user.id =:id and a.status = 'SUCCESS'
      """)
  List<Attempt> findSuccessAttemptByUserId(UUID id);

}

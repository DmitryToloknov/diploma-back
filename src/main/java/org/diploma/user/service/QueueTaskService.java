package org.diploma.user.service;

import lombok.RequiredArgsConstructor;
import org.diploma.user.Entity.QueueTask;
import org.diploma.user.exception.CustomException;
import org.diploma.user.repository.QueueTaskRepository;
import org.springframework.stereotype.Service;

import static org.diploma.user.exception.ExceptionMessageConstants.QUEUE_TASK_WITH_TASK_ID_EXIST;

@Service
@RequiredArgsConstructor
public class QueueTaskService {

  private final QueueTaskRepository queueTaskRepository;

  public void createQueueTask(Long taskId) {
    queueTaskRepository.findByTaskId(taskId).ifPresent(queueTask -> {
      throw new CustomException(QUEUE_TASK_WITH_TASK_ID_EXIST);
    });

    var queueTask = new QueueTask();
    queueTask.setTaskId(taskId);
    queueTask.setAttemptNumber(0);
    queueTaskRepository.save(queueTask);
  }

  public QueueTask findFirst() {
    return queueTaskRepository.findFirst();
  }

  public void updateAttemptNumber(QueueTask queueTask) {
    queueTask.setAttemptNumber(queueTask.getAttemptNumber() + 1);
    queueTaskRepository.save(queueTask);
  }

  public void deleteQueueTask(QueueTask queueTask) {
    queueTaskRepository.deleteById(queueTask.getId());
  }
}

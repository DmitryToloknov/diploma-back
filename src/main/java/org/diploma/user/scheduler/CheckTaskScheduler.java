package org.diploma.user.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.diploma.user.Util.TaskStatus;
import org.diploma.user.service.AttemptService;
import org.diploma.user.service.CheckTask;
import org.diploma.user.service.QueueTaskService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CheckTaskScheduler {

  private final QueueTaskService queueTaskService;
  private final AttemptService attemptService;
  private final CheckTask checkTask;

  @Scheduled(fixedDelay = 1000)
  public void runTask() {
    var queueTask = queueTaskService.findFirst();
    if(queueTask == null) {
      return;
    }
    if(queueTask.getAttemptNumber()>=5) {
      queueTaskService.deleteQueueTask(queueTask);
      return;
    }
    queueTaskService.updateAttemptNumber(queueTask);
    try {
      checkTask.checkTask(queueTask.getTaskId());
      queueTaskService.deleteQueueTask(queueTask);
    } catch (Exception e) {
      log.error("Ошибка запуска тест кейсов у задачи {}", queueTask.getTaskId(), e);
    }
  }

  @Scheduled(fixedDelay = 1000)
  public void runAttempt() {
    var attempt = attemptService.findFirst();
    if(attempt == null) {
      return;
    }
    attemptService.updateAttemptNumber(attempt);
    try {
      checkTask.checkAttempt(attempt);
    } catch (Exception e) {
      attempt.setStatus(TaskStatus.ERROR);
      attemptService.save(attempt);
      log.error("Ошибка запуска тест кейсов у задачи {}", attempt, e);
    }
  }

}

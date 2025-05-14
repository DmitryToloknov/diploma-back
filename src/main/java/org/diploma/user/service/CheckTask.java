package org.diploma.user.service;

import lombok.RequiredArgsConstructor;
import org.diploma.user.Entity.Attempt;
import org.diploma.user.Util.TaskStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class CheckTask {

  private final TaskService taskService;
  private final TestCaseService testCaseService;
  private final AttemptService attemptService;

  public void checkTask(Long id) throws IOException, InterruptedException {
    var task = taskService.findById(id);
    var code = task.getCode();
    var taskLanguage = task.getTaskLanguage();
    var timeLimit = task.getTimeLimit();
    var memoryLimit = task.getMemoryLimit();

    var testCases = testCaseService.getTestCaseByTaskId(task.getId());

    for (var testCase : testCases) {
      var result = DockerCodeRunner.runCode(code, timeLimit, memoryLimit, testCase.getInCase(), taskLanguage);
      testCase.setOutCase(result);
      testCaseService.save(testCase);
    }
  }

  public void checkAttempt(Attempt attempt) {
    attempt.setStatus(TaskStatus.IN_PROGRESS);
    attemptService.save(attempt);
    try {
      var task = attempt.getTask();
      var code = attempt.getCode();
      var timeLimit = task.getTimeLimit();
      var memoryLimit = task.getMemoryLimit();
      var taskLanguage = attempt.getTaskLanguage();
      var testCases = testCaseService.getTestCaseByTaskId(task.getId());
      for (var testCase : testCases) {
        var result = DockerCodeRunner.runCode(code, timeLimit, memoryLimit, testCase.getInCase(), taskLanguage);
        var outCase = testCase.getOutCase();
        if (!outCase.equals(result)) {
          attempt.setErrorTestCase(testCase);
          attempt.setErrorMessage(result);
          throw new RuntimeException("Ответ не совпадает!");
        }
      }
      attempt.setStatus(TaskStatus.REVIEW);
    } catch (Exception e) {
      attempt.setStatus(TaskStatus.ERROR);
    } finally {
      attemptService.save(attempt);
    }
  }

}

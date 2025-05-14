package org.diploma.user.service;

import lombok.RequiredArgsConstructor;
import org.diploma.user.Entity.TestCase;
import org.diploma.user.controller.testcase.dto.RequestTestCase;
import org.diploma.user.controller.testcase.dto.ResponseTestCase;
import org.diploma.user.exception.CustomException;
import org.diploma.user.mapper.TestCaseMapper;
import org.diploma.user.repository.TestCaseRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static org.diploma.user.exception.ExceptionMessageConstants.TEST_CASE_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class TestCaseService {
  private final TestCaseRepository testCaseRepository;
  private final TaskService taskService;
  private final TestCaseMapper testCaseMapper;

  public void createTestCase(Long taskId, UUID creator) {
    var task = taskService.findById(taskId);
    var testCase = new TestCase();
    testCase.setTask(task);
    testCaseRepository.save(testCase);
  }

  public void deleteTestCase(UUID id) {
    var testCase = testCaseRepository.findById(id);
    testCase.ifPresent(test -> {
      test.setDeleted(true);
      testCaseRepository.save(test);
    });
  }

  public List<TestCase> getTestCaseByTaskId(Long taskId) {
    return testCaseRepository.findTestCases(taskId);
  }

  public List<ResponseTestCase> getTestCase(Long taskId) {
    var testCases = testCaseRepository.findTestCases(taskId);
    return testCaseMapper.map(testCases);
  }

  public ResponseTestCase getTestCase(UUID id) {
    var testCase = testCaseRepository.findByIdAndDeletedFalse(id).orElseThrow(() -> new CustomException(TEST_CASE_NOT_FOUND));
    return testCaseMapper.map(testCase);
  }

  public void updateTestCase(RequestTestCase requestTestCase) {
    var testCase = testCaseRepository.findByIdAndDeletedFalse(requestTestCase.getId()).orElseThrow(() -> new CustomException(TEST_CASE_NOT_FOUND));
    testCase.setInCase(requestTestCase.getInCase());
    testCase.setOutCase("");
    testCaseRepository.save(testCase);
  }

  public void save(TestCase testCase) {
    testCaseRepository.save(testCase);
  }

}

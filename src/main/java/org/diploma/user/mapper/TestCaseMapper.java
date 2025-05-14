package org.diploma.user.mapper;

import org.diploma.user.Entity.TestCase;
import org.diploma.user.controller.testcase.dto.ResponseTestCase;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface TestCaseMapper {

  ResponseTestCase map(TestCase testCase);

  List<ResponseTestCase> map(List<TestCase> testCase);
}

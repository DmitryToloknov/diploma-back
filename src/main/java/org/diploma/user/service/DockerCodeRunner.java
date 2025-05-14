package org.diploma.user.service;

import lombok.RequiredArgsConstructor;
import org.diploma.user.Util.TaskLanguage;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class DockerCodeRunner {

  private static final String CPP_DOCKER_IMAGE = "dtoloknov068/cpp-sandbox:latest";
  private static final String PYTHON_DOCKER_IMAGE = "dtoloknov068/python-sandbox:latest";

  public static String runCode(String code, Integer timeLimit, Integer memoryLimit, String inputData, TaskLanguage taskLanguage) throws IOException, InterruptedException {
    if (taskLanguage == TaskLanguage.CPP) {
      return runCppCode(code, timeLimit, memoryLimit, inputData);
    } else {
      return runPythonCode(code, timeLimit, memoryLimit, inputData);
    }
  }

  public static String runCppCode(String code, Integer timeLimit, Integer memoryLimit, String inputData) throws IOException, InterruptedException {
    return runInDocker(code, inputData, timeLimit, memoryLimit, CPP_DOCKER_IMAGE, "main.cpp");
  }

  public static String runPythonCode(String code, Integer timeLimit, Integer memoryLimit, String inputData) throws IOException, InterruptedException {
    return runInDocker(code, inputData, timeLimit, memoryLimit, PYTHON_DOCKER_IMAGE, "main.py");
  }

  private static String runInDocker(String code, String inputData, Integer timeLimit, Integer memoryLimit, String dockerImage, String fileName)
      throws IOException, InterruptedException {

    String jobId = UUID.randomUUID().toString();
    Path tempDir = Files.createTempDirectory("job_" + jobId);

    Path codePath = tempDir.resolve(fileName);
    Path inputPath = tempDir.resolve("input.txt");

    Files.write(codePath, code.getBytes());
    Files.write(inputPath, inputData.getBytes());

    Process process = getProcess(memoryLimit, dockerImage, tempDir);

    boolean finished = process.waitFor(timeLimit, TimeUnit.MILLISECONDS);
    if (!finished) {
      process.destroyForcibly();
      deleteDirectory(tempDir);
      return "Время выполнения превысило лимит";
    }

    String result = new String(process.getInputStream().readAllBytes());
    String errors = new String(process.getErrorStream().readAllBytes());

    int exitCode = process.waitFor();

    deleteDirectory(tempDir);

    if (exitCode != 0) {
      if (errors.contains("Killed") || errors.contains("killed signal") || errors.contains("fatal error") && errors.contains("cc1plus")) {
        System.out.println(errors);
        return "Превышен лимит памяти";
      }
      throw new RuntimeException("Ошибка выполнения: " + errors);
    }

    return result.trim();
  }

  private static Process getProcess(Integer memoryLimit, String dockerImage, Path tempDir) throws IOException {
//    ProcessBuilder processBuilder = new ProcessBuilder(
//        "docker", "run",
//        "--rm",
//        "--net=none",
//        "--cpus=1.0",
//        "--memory=" + memoryLimit + "m",
//        "--memory-swap=" + memoryLimit + "m",
//        "--pids-limit=64",
//        "--read-only",
//        "--security-opt", "no-new-privileges=true",
//        "-v", tempDir.toAbsolutePath() + ":/sandbox",
//        dockerImage
//    );

    ProcessBuilder processBuilder = new ProcessBuilder(
        "docker", "run",
        "--rm",
        "--net=none",
        "--cpus=1.0",
        "--memory=" + memoryLimit + "m",
        "--memory-swap=" + memoryLimit + "m",
        "--pids-limit=64",
//        "--read-only",
        "--security-opt", "no-new-privileges=true",
        "-v", tempDir.toAbsolutePath() + ":/sandbox",
        dockerImage,
        "sh", "-c",
        "cd /sandbox && g++ main.cpp -o main && ./main < input.txt"
    );

    // Устанавливаем таймаут внешне через Java
    Process process = processBuilder.start();
    return process;
  }

  private static void deleteDirectory(Path path) throws IOException {
    if (Files.exists(path)) {
      Files.walk(path)
          .sorted(Comparator.reverseOrder())
          .forEach(p -> {
            try {
              Files.delete(p);
            } catch (IOException e) {
              e.printStackTrace();
            }
          });
    }
  }

}

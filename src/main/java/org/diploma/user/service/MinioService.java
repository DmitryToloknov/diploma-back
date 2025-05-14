package org.diploma.user.service;

import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.http.Method;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.diploma.user.exception.CustomException;
import org.springframework.stereotype.Service;

import java.io.InputStream;

import static org.diploma.user.exception.ExceptionMessageConstants.FILE_ERROR;

@Slf4j
@Service
@RequiredArgsConstructor
public class MinioService {

  private final MinioClient minioClient;

  public void uploadPhoto(String objectName, InputStream fileInputStream, String contentType, String bucket)
      throws Exception {
    minioClient.putObject(
        PutObjectArgs.builder()
            .object(objectName)
            .stream(fileInputStream, fileInputStream.available(), -1)
            .contentType(contentType)
            .bucket(bucket)
            .build()
    );
  }

  public String generateTemporaryUrl(String objectName, String bucket) {
    try {
      return minioClient.getPresignedObjectUrl(
          GetPresignedObjectUrlArgs.builder()
              .object(objectName)
              .bucket(bucket)
              .method(Method.GET)
              .expiry(604800)
              .build()
      );
    } catch (Exception e) {
      log.error("Ошибка получении ссылки", e);
      throw new CustomException(FILE_ERROR);
    }
  }

}

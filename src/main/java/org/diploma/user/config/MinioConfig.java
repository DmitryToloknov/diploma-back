package org.diploma.user.config;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinioConfig {

  @Value("${minio.url}")
  private String minioUrl;
  @Value("${minio.accessKey}")
  private String accessKey;
  @Value("${minio.secretKey}")
  private String secretKey;
  @Value("${minio.secure}")
  private Boolean minioSecure;

  @Bean
  public MinioClient minioClient() {
    return MinioClient.builder().credentials(accessKey, secretKey)
      .endpoint(minioUrl, 9000, minioSecure).build();
  }
}

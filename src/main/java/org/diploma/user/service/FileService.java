package org.diploma.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.diploma.user.controller.file.dto.FileLinkResponseDto;
import org.diploma.user.exception.CustomException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

import static org.diploma.user.exception.ExceptionMessageConstants.FILE_ERROR;
import static org.diploma.user.exception.ExceptionMessageConstants.FILE_NOT_FOUND;
import static org.diploma.user.exception.ExceptionMessageConstants.FILE_TYPE_ERROR;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileService {

  private final MinioService minioService;
  private static final String IMG_BUCKET = "images";

  @Value("${minio.get-url}")
  private String pathUrl;

  public String uploadImgFile(MultipartFile multipartFile) {
    if (multipartFile.isEmpty()) {
      throw new CustomException(FILE_NOT_FOUND);
    }
    if (multipartFile.getOriginalFilename() == null) {
      throw new CustomException(FILE_ERROR);
    }
    String originalFilename = StringUtils.cleanPath(multipartFile.getOriginalFilename());
    String extension = getFileExtension(originalFilename);
    if (!isValidImageExtension(extension)) {
      throw new CustomException(FILE_TYPE_ERROR);
    }
    String uniqueFilename = UUID.randomUUID() + "." + extension;
    try {
      minioService.uploadPhoto(uniqueFilename, multipartFile.getInputStream(), multipartFile.getContentType(),
          IMG_BUCKET);
    } catch (Exception e) {
      log.error("Ошибка сохранения файла", e);
      throw new CustomException(FILE_ERROR);
    }
    return uniqueFilename;
  }

  public FileLinkResponseDto getUrlFile(String object) {
    return new FileLinkResponseDto(pathUrl+object, "success");
  }

  private String getFileExtension(String filename) {
    int lastDotIndex = filename.lastIndexOf(".");
    return (lastDotIndex == -1) ? "" : filename.substring(lastDotIndex + 1).toLowerCase();
  }

  private boolean isValidImageExtension(String extension) {
    return extension.equals("png") || extension.equals("jpeg") || extension.equals("jpg");
  }

}

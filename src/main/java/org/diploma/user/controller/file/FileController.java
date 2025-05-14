package org.diploma.user.controller.file;

import lombok.RequiredArgsConstructor;
import org.diploma.user.controller.file.dto.FileLinkResponseDto;
import org.diploma.user.service.FileService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/file")
public class FileController {

  private final FileService fileService;

  @PostMapping("/img/upload")
  @PreAuthorize("hasRole('NEWS_UPDATE') || hasRole('ADMIN')")
  public ResponseEntity<FileLinkResponseDto> uploadImgFile(@RequestParam("file") MultipartFile file) {
    return ResponseEntity.ok(fileService.getUrlFile(fileService.uploadImgFile(file)));
  }

}

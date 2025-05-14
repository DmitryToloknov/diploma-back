package org.diploma.user.controller.file.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FileLinkResponseDto {
  private String fileUrl;
  private String status;
}

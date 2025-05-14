package org.diploma.user.controller.group.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.Year;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GroupInfoResponse {

  private UUID id;
  private String name;
  private String shortName;
  private Year year;
  private UUID curatorId;
  private String curatorName;
  private UUID creatorId;
  private String creatorNames;
  private Integer countCourse;
  private LocalDateTime createdDateTime;
  private LocalDateTime updatedDateTime;

}

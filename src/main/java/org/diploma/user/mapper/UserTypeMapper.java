package org.diploma.user.mapper;

import org.diploma.user.Entity.UserType;
import org.diploma.user.controller.userType.dto.UserTypeInfoResponse;
import org.diploma.user.controller.userType.dto.UserTypeRequest;
import org.diploma.user.controller.userType.dto.UserTypeResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper
public interface UserTypeMapper {

  UserTypeResponse toUserTypeResponse(UserType userType);

  List<UserTypeResponse> toUserTypeResponse(List<UserType> userType);

  @Mapping(target = "creatorId", source = "userType.creator.id")
  UserTypeInfoResponse toUserTypeInfoResponse(UserType userType);

  UserType toEntity(UserTypeRequest userTypeRequest);

  void updateEntityFromUserTypeRequest(UserTypeRequest userTypeRequest, @MappingTarget UserType userType);
}

package org.diploma.user.mapper;

import org.diploma.user.Entity.Group;
import org.diploma.user.Entity.User;
import org.diploma.user.controller.group.dto.GroupInfoResponse;
import org.diploma.user.controller.group.dto.GroupRequest;
import org.diploma.user.controller.group.dto.GroupResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.util.List;
import java.util.UUID;

@Mapper
public interface GroupMapper {

  GroupResponse toGroupResponse(Group group);

  List<GroupResponse> toGroupResponse(List<Group> group);

  @Mapping(target = "curatorId", source = "group.curator.id")
  @Mapping(target = "creatorId", source = "group.creator.id")
  @Mapping(target = "curatorName", source = "group.curator", qualifiedByName = "getUserName")
  @Mapping(target = "creatorNames", source = "group.creator", qualifiedByName = "getUserName")
  GroupInfoResponse toGroupInfoResponse(Group group);


  List<GroupInfoResponse> toGroupInfoResponse(List<Group> group);

  Group toEntity(GroupRequest groupCreateRequest);

  void updateEntityFromGroupRequest(GroupRequest groupRequest, @MappingTarget Group group);

  @Named("getUserName")
  default String getCuratorName(User user) {
    if (user != null) {
      return user.getName() + " " + user.getSurname();  // Можно вернуть дефолтное значение
    }
   return null;
  }

}

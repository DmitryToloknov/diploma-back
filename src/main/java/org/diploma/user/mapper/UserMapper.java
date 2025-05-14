package org.diploma.user.mapper;

import org.diploma.user.Entity.User;
import org.diploma.user.Entity.UserType;
import org.diploma.user.controller.auth.dto.RegisterRequest;
import org.diploma.user.controller.user.dto.FullNameDto;
import org.diploma.user.controller.user.dto.ResponseCurator;
import org.diploma.user.controller.user.dto.ResponseUser;
import org.diploma.user.controller.user.dto.ResponseUserInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Mapper
public interface UserMapper {

  @Mapping(target = "status", expression = "java(true)")
  User toUser(RegisterRequest requestRegister);

  FullNameDto toFullNameDto (User user);

  @Mapping(target = "userName", expression = "java(user.getName() + \" \" + user.getSurname())")
  ResponseCurator toResponseCurator(User user);

  List<ResponseCurator> toResponseCurator(List<User> user);

  @Mapping(target = "groupName", source = "groupName")
  ResponseUser toResponseUser(User user, String groupName);

  default List<UUID> map(Set<UserType> value) {
    return value.stream().map(UserType::getId).toList();
  }

  @Mapping(target = "userName", expression = "java(user.getName() + \" \" + user.getSurname())")
  ResponseUserInfo mapToResponseUserInfo(User user);
  List<ResponseUserInfo> mapToResponseUserInfo(List<User> users);

}

package org.diploma.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.diploma.user.Entity.User;
import org.diploma.user.Entity.UserType;
import org.diploma.user.Predicate.UserPredicate;
import org.diploma.user.Util.Role;
import org.diploma.user.controller.user.dto.FullNameDto;
import org.diploma.user.controller.user.dto.ResponseCurator;
import org.diploma.user.controller.user.dto.ResponseUser;
import org.diploma.user.controller.user.dto.ResponseUserInfo;
import org.diploma.user.controller.user.dto.RoleResponseForUser;
import org.diploma.user.controller.user.dto.UpdateUserRequest;
import org.diploma.user.controller.user.dto.UserDeleteRequest;
import org.diploma.user.controller.user.dto.UserFilterRequest;
import org.diploma.user.exception.CustomException;
import org.diploma.user.mapper.UserMapper;
import org.diploma.user.repository.GroupRepository;
import org.diploma.user.repository.UserRepository;
import org.diploma.user.repository.UserRepositoryCustom;
import org.diploma.user.repository.UserTypeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.diploma.user.exception.ExceptionMessageConstants.GROUP_NOT_FOUND;
import static org.diploma.user.exception.ExceptionMessageConstants.USER_CREATOR_NOT_FOUND;
import static org.diploma.user.exception.ExceptionMessageConstants.USER_NOT_FOUND;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final UserRepositoryCustom userRepositoryCustom;
  private final UserMapper userMapper;
  private final UserPredicate userPredicate;
  private final ClientService clientService;
  private final GroupRepository groupRepository;
  private final UserTypeRepository userTypeRepository;

  public void save(User user) {
    userRepository.save(user);
  }

  public Optional<User> findByLogin(String login) {
    return userRepository.findByLoginAndIsStatusTrue(login);
  }

  public Optional<User> findById(UUID id) {
    return userRepository.findById(id);
  }

  public User findUserOrThrow(UUID id) {
    return userRepository.findById(id).orElseThrow(() -> new CustomException(USER_CREATOR_NOT_FOUND));
  }

  public FullNameDto getFullName(UUID id) {
    return userMapper.toFullNameDto(userRepository.findById(id).orElseThrow(() -> new CustomException(USER_NOT_FOUND)));
  }

  public List<ResponseCurator> getCurators() {
    return userMapper.toResponseCurator(userRepository.findCurators());
  }

  public long numberUsers(UserFilterRequest userFilterRequest) {
    var predicate = userPredicate.buildPredicate(userFilterRequest);
    return userRepository.count(predicate);
  }

  @Transactional
  public void updateUser(UUID id, UpdateUserRequest updateUserRequest) {
    var user = findById(id).orElseThrow(() -> new CustomException(USER_NOT_FOUND));
    user.setName(updateUserRequest.getName());
    user.setSurname(updateUserRequest.getSurname());
    user.setPhone(updateUserRequest.getPhone());
    if (updateUserRequest.getGroupId() != null) {
      var group = groupRepository.findAllByIdAndDeletedFalse(
          updateUserRequest.getGroupId()).orElseThrow(() -> new CustomException(GROUP_NOT_FOUND));
      user.setGroup(group);
    } else {
      user.setGroup(null);
    }

    if (!updateUserRequest.getTypes().isEmpty()) {
      var types = userTypeRepository.findAllByIdInAndDeletedFalse(updateUserRequest.getTypes());
      user.setTypes(types);
    } else {
      user.setTypes(null);
    }
  }

  public List<ResponseUser> getUsers(UserFilterRequest userFilterRequest, int page, int perPage) {
    var predicate = userPredicate.buildPredicate(userFilterRequest);
    var users = userRepository.findByIdIn(userRepositoryCustom.findAllIds(predicate, page - 1, perPage));
    List<ResponseUser> usersInfo = new ArrayList<>();
    users.forEach(user -> {
      var group = user.getGroup();
      String groupName = "—";
      if (group != null) {
        groupName = String.format("%s %s", group.getYear(), group.getShortName());
      }
      usersInfo.add(userMapper.toResponseUser(user, groupName));
    });

    return usersInfo;
  }

  public ResponseUser getUserById(UUID id) {
    var user = findById(id).orElseThrow(() -> new CustomException(USER_NOT_FOUND));
    var group = user.getGroup();
    String groupName = "—";
    UUID groupId = null;
    if (group != null) {
      groupName = String.format("%s %s", group.getYear(), group.getShortName());
      groupId = group.getId();
    }

    var responseUser = userMapper.toResponseUser(user, groupName);
    responseUser.setGroupId(groupId);

    var types = user.getTypes();
    if (!types.isEmpty()) {
      var typeIds = types.stream().map(UserType::getId).toList();
      responseUser.setTypes(typeIds);
    } else {
      responseUser.setTypes(List.of());
    }
    return responseUser;
  }

  public void deleteById(UUID id, UserDeleteRequest userDeleteRequest) {
    findById(id).ifPresent(user -> {
      user.setStatus(false);
      user.setDeletionReason(userDeleteRequest.getDeletionReason());
      userRepository.save(user);
    });
  }

  public List<String> getRolesUserById(UUID id) {
    return clientService.getRolesByUserId(id);
  }

  public List<RoleResponseForUser> getFullRoles() {
    return Role.getFullRole();
  }

  public void updateRoles(List<String> roles, UUID id) {
    clientService.updateRole(roles, id);
  }

  public List<ResponseUserInfo> getUsersByGroupId(UUID groupId) {
    var users = userRepository.findByGroup(groupId);
    return userMapper.mapToResponseUserInfo(users);
  }

}

package org.diploma.user.service;

import lombok.RequiredArgsConstructor;
import org.diploma.user.Entity.UserType;
import org.diploma.user.controller.userType.dto.UserTypeInfoResponse;
import org.diploma.user.controller.userType.dto.UserTypeRequest;
import org.diploma.user.controller.userType.dto.UserTypeResponse;
import org.diploma.user.exception.CustomException;
import org.diploma.user.mapper.UserTypeMapper;
import org.diploma.user.repository.UserTypeRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.diploma.user.exception.ExceptionMessageConstants.USER_TYPE_EXIST;
import static org.diploma.user.exception.ExceptionMessageConstants.USER_TYPE_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class UserTypeService {
  private final UserTypeMapper userTypeMapper;

  private final UserTypeRepository userTypeRepository;

  private final UserService userService;

  @Value("${server.url}")
  private String url;

  public List<UserType> findAll() {
    return userTypeRepository.findAllByDeletedFalse();
  }

  public Optional<UserType> findById(UUID id) {
    return userTypeRepository.findByIdAndDeletedFalse(id);
  }


  public Set<UserType> findAllByIdIn(List<UUID> ids) {
    return userTypeRepository.findAllByIdInAndDeletedFalse(ids);
  }

  public List<UserTypeResponse> searchUserTypeResponse() {
    return userTypeMapper.toUserTypeResponse(findAll());
  }

  public UserTypeInfoResponse searchUserTypeInfoResponse(UUID id) {
    var userType = findById(id).orElseThrow(() -> new CustomException(USER_TYPE_NOT_FOUND));
    return userTypeMapper.toUserTypeInfoResponse(userType);
  }

  public URI createUserType(UserTypeRequest userTypeRequest, UUID creatorId) {
    userTypeRepository.findByNameAndDeletedFalse(userTypeRequest.getName()).ifPresent(userType -> {
      throw new CustomException(USER_TYPE_EXIST);
    });
    var creator = userService.findUserOrThrow(creatorId);
    var userType = userTypeMapper.toEntity(userTypeRequest);
    userType.setCreator(creator);
    userTypeRepository.save(userType);
    return URI.create(url + "user-type/" + userType.getId());
  }

  public void updateUserType(UserTypeRequest userTypeRequest, UUID id) {
    var userType = findById(id).orElseThrow(() -> new CustomException(USER_TYPE_NOT_FOUND));
    userTypeMapper.updateEntityFromUserTypeRequest(userTypeRequest, userType);
    userTypeRepository.save(userType);
  }

  @Transactional
  public void deleteById(UUID id) {
    userTypeRepository.findById(id).ifPresent(userType -> userType.setDeleted(true));
  }

}

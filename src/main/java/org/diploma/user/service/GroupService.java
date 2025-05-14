package org.diploma.user.service;

import lombok.RequiredArgsConstructor;
import org.diploma.user.Entity.Group;
import org.diploma.user.controller.group.dto.GroupInfoResponse;
import org.diploma.user.controller.group.dto.GroupRequest;
import org.diploma.user.controller.group.dto.GroupResponse;
import org.diploma.user.exception.CustomException;
import org.diploma.user.mapper.GroupMapper;
import org.diploma.user.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.diploma.user.exception.ExceptionMessageConstants.GROUP_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class GroupService {

  private final UserService userService;

  private final GroupRepository groupRepository;

  private final GroupMapper groupMapper;

  @Value("${server.url}")
  private String url;

  public List<Group> findAll() {
    return groupRepository.findAllByDeletedFalse();
  }

  public Optional<Group> findById(UUID id) {
    return groupRepository.findAllByIdAndDeletedFalse(id);
  }

  public Set<Group> findById(List<UUID> id) {
    return groupRepository.findAllByIdInAndDeletedFalse(id);
  }

  public List<GroupResponse> searchGroupResponseList() {
    LocalDate now = LocalDate.now();
    List<GroupResponse> groupResponses = new ArrayList<>();
    List<Group> groups = findAll();
    groups.forEach(group -> {
      var numberCourse = calculateCourse(group.getYear(), now);
      if (numberCourse <= group.getCountCourse()) {
        var name = String.format("%s%s", numberCourse, group.getShortName());
        groupResponses.add(new GroupResponse(group.getId(), name));
      }
    });
    return groupResponses;
  }

  private Integer calculateCourse(Year startYear, LocalDate now) {
    int currentAcademicYear = now.getMonth().getValue() >= Month.SEPTEMBER.getValue()
        ? now.getYear()
        : now.getYear() - 1;
    return currentAcademicYear - startYear.getValue() + 1;
  }

  public List<GroupInfoResponse> getGroupsForSetting() {
    return groupMapper.toGroupInfoResponse(groupRepository.findAllByDeletedFalse());
  }

  public GroupInfoResponse searchGroupInfoResponse(UUID id) {
    var group = findById(id).orElseThrow(() -> new CustomException(GROUP_NOT_FOUND));
    return groupMapper.toGroupInfoResponse(group);
  }

  public URI createGroup(GroupRequest groupCreateRequest, UUID creatorId) {
    var creator = userService.findUserOrThrow(creatorId);
    var group = groupMapper.toEntity(groupCreateRequest);
    if (groupCreateRequest.getCuratorId() != null) {
      var curator = userService.findUserOrThrow(groupCreateRequest.getCuratorId());
      group.setCurator(curator);
    }
    group.setCreator(creator);

    groupRepository.save(group);
    return URI.create(url + "group/" + group.getId());
  }

  public void updateGroup(GroupRequest groupRequest, UUID id) {

    var group = groupRepository.findById(id).orElseThrow(() -> new CustomException(GROUP_NOT_FOUND));
    groupMapper.updateEntityFromGroupRequest(groupRequest, group);
    if (groupRequest.getCuratorId() != null) {
      var curator = userService.findUserOrThrow(groupRequest.getCuratorId());
      group.setCurator(curator);
    } else {
      group.setCurator(null);
    }
    groupRepository.save(group);
  }

  @Transactional
  public void deleteById(UUID id) {
    groupRepository.findById(id).ifPresent(group -> group.setDeleted(true));
  }

}

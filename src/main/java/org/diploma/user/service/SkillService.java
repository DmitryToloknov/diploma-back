package org.diploma.user.service;

import lombok.RequiredArgsConstructor;
import org.diploma.user.Entity.Skill;
import org.diploma.user.controller.skill.dto.CreateSkillDto;
import org.diploma.user.controller.skill.dto.ResponseSkill;
import org.diploma.user.controller.skill.dto.SkillUser;
import org.diploma.user.mapper.SkillMapper;
import org.diploma.user.repository.AttemptRepository;
import org.diploma.user.repository.SkillRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SkillService {

  private final SkillRepository skillRepository;
  private final AttemptRepository attemptRepository;
  private final UserService userService;
  private final SkillMapper skillMapper;

  public void createSkill(CreateSkillDto createSkillDto, UUID creatorId) {
    var skill = new Skill();
    skill.setName(createSkillDto.getName());
    skill.setCreator(userService.findUserOrThrow(creatorId));
    skillRepository.save(skill);
  }

  public List<ResponseSkill> getSkills() {
    var skills = skillRepository.findByIsDeletedFalse();
    return skillMapper.map(skills);
  }

  public Set<Skill> getStallsByIds(List<UUID> ids) {
    return skillRepository.findByIdsAndIsDeletedFalse(ids);
  }

  public List<SkillUser> getUserSkills(UUID userId) {
    var attempts = attemptRepository.findSuccessAttemptByUserId(userId);
    var skillsCount = attempts.stream()
        .flatMap(attempt -> attempt.getTask().getSkills().stream())
        .collect(Collectors.groupingBy(
            Skill::getName,
            Collectors.counting()
        ));
    List<SkillUser> skillUsers = new ArrayList<>();
    skillsCount.forEach((key, value) -> {
      var skillUser = new SkillUser();
      skillUser.setName(key);
      skillUser.setCount(value);
      skillUsers.add(skillUser);
    });
    skillUsers.sort(Comparator.comparing(SkillUser::getCount).reversed());
    return skillUsers;
  }
}

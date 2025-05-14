package org.diploma.user.service;

import lombok.RequiredArgsConstructor;
import org.diploma.user.Entity.Skill;
import org.diploma.user.controller.skill.dto.CreateSkillDto;
import org.diploma.user.controller.skill.dto.ResponseSkill;
import org.diploma.user.mapper.SkillMapper;
import org.diploma.user.repository.SkillRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SkillService {

  private final SkillRepository skillRepository;
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
}

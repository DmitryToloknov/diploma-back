package org.diploma.user.mapper;

import org.diploma.user.Entity.Skill;
import org.diploma.user.controller.skill.dto.ResponseSkill;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface SkillMapper {

  ResponseSkill map(Skill skill);

  List<ResponseSkill> map(List<Skill> skills);

}

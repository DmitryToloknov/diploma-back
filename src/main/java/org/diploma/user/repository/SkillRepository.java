package org.diploma.user.repository;

import org.diploma.user.Entity.Skill;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Repository
public interface SkillRepository extends CrudRepository<Skill, UUID> {

  List<Skill> findByIsDeletedFalse();
  @Query("select s from Skill s where s.id in :ids and s.isDeleted = false ")
  Set<Skill> findByIdsAndIsDeletedFalse(List<UUID> ids);

}

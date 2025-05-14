package org.diploma.user.repository;

import org.diploma.user.Entity.Group;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface GroupRepository extends JpaRepository<Group, UUID> {

  @EntityGraph(attributePaths = {"curator", "creator"})
  Optional<Group> findAllByIdAndDeletedFalse(UUID id);

  Set<Group> findAllByIdInAndDeletedFalse(List<UUID> ids);

  @EntityGraph(attributePaths = {"curator", "creator"})
  List<Group> findAllByDeletedFalse();

}

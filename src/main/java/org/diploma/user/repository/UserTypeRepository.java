package org.diploma.user.repository;

import org.diploma.user.Entity.UserType;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface UserTypeRepository extends JpaRepository<UserType, UUID> {
  Set<UserType> findAllByIdInAndDeletedFalse(List<UUID> ids);

  Optional<UserType> findByNameAndDeletedFalse(String name);

  List<UserType> findAllByDeletedFalse();
  @EntityGraph(attributePaths = {"creator"})
  Optional<UserType> findByIdAndDeletedFalse(UUID id);
}

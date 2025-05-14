package org.diploma.user.repository;

import org.diploma.user.Entity.News;
import org.diploma.user.controller.news.dto.NewsPreviewResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface NewsRepository extends JpaRepository<News, UUID> {

  Optional<News> findByIdAndIsDeletedFalse(UUID id);

  @Modifying
  @Transactional
  @Query("UPDATE News n SET n.isDeleted = true WHERE n.id = :id")
  int markAsDeleted(UUID id);

  @Query("""
        SELECT new org.diploma.user.controller.news.dto.NewsPreviewResponse(
            n.id, n.name, n.description, n.datePublication, u.id, CONCAT(u.name, ' ', u.surname), n.isStatus
        ) 
        FROM News n
        JOIN User u ON u.id = n.creatorId
        WHERE n.isDeleted = false 
          AND (:hasEditRole = true OR n.isStatus = true )
        ORDER BY COALESCE(n.datePublication, n.updatedDateTime) desc 
        LIMIT :limit OFFSET :offset
    """)
  List<NewsPreviewResponse> findAllFiltered(boolean hasEditRole, int limit, int offset);

  @Query("""
        SELECT count(*)
        FROM News n
        WHERE n.isDeleted = false 
          AND (:hasEditRole = true OR n.isStatus = true )
    """)
  int count(boolean hasEditRole);
}

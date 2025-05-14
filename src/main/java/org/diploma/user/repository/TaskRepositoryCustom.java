package org.diploma.user.repository;

import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.diploma.user.Entity.QTask;
import org.diploma.user.Entity.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskRepositoryCustom {

  private final EntityManager entityManager;

  public List<Long> findAllIds(Predicate predicate, long page, long perPage) {
    JPAQuery<User> query = new JPAQuery<>(entityManager);
    var task = QTask.task;
    return query.select(task.id)
        .from(task)
        .where(predicate)
        .offset(page * perPage)
        .orderBy(task.id.desc())
        .limit(perPage)
        .fetch();
  }

}

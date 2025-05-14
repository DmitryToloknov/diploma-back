package org.diploma.user.repository;

import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.diploma.user.Entity.QUser;
import org.diploma.user.Entity.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserRepositoryCustom {

  private final EntityManager entityManager;

  public List<UUID> findAllIds(Predicate predicate, long page, long perPage) {
    JPAQuery<User> query = new JPAQuery<>(entityManager);
    var qUser = QUser.user;
    return query.select(qUser.id)
        .from(qUser)
        .where(predicate)
        .offset(page * perPage)
        .orderBy(qUser.id.desc())
        .limit(perPage)
        .fetch();
  }

}

package org.diploma.user.repository;

import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.diploma.user.Entity.Course;
import org.diploma.user.Entity.QCourse;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class CourseRepositoryCustom {

  private final EntityManager entityManager;

  public List<UUID> findAllIds(Predicate predicate, long page, long perPage) {
    JPAQuery<Course> query = new JPAQuery<>(entityManager);
    var course = QCourse.course;
    return query.select(course.id)
        .from(course)
        .where(predicate)
        .offset(page * perPage)
        .orderBy(course.id.desc())
        .limit(perPage)
        .fetch();
  }

}

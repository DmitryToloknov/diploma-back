package org.diploma.user.Predicate;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import org.diploma.user.Entity.QUser;
import org.diploma.user.controller.user.dto.UserFilterRequest;
import org.springframework.stereotype.Component;

@Component
public class UserPredicate {

  public Predicate buildPredicate(UserFilterRequest userFilterRequest) {
    var predicate = new BooleanBuilder();
    var user = QUser.user;
    if (!userFilterRequest.getGroupIds().isEmpty()) {
      predicate.and(user.group.id.in(userFilterRequest.getGroupIds()));
    }

    if (!userFilterRequest.getUserTypeIds().isEmpty()) {
      predicate.and(user.types.any().id.in(userFilterRequest.getUserTypeIds()));
    }

    if (userFilterRequest.getName() != null && !userFilterRequest.getName().isEmpty()) {
      predicate.and(user.name.containsIgnoreCase( userFilterRequest.getName())
          .or(user.surname.containsIgnoreCase( userFilterRequest.getName())));
    }

    if(userFilterRequest.getOnlyActiveUser() != null && userFilterRequest.getOnlyActiveUser()) {
      predicate.and(user.isStatus.eq(true));
    }

    return predicate;
  }

}

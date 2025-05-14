package org.diploma.user.mapper;

import org.diploma.user.Entity.Course;
import org.diploma.user.Entity.Group;
import org.diploma.user.controller.course.dto.CourseInfo;
import org.diploma.user.controller.course.dto.ResponseCourse;
import org.diploma.user.controller.course.dto.ResponseCourseInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Mapper
public interface CourseMapper {

  @Mapping(target = "groups", source = "groups", qualifiedByName = "getGroupsId")
  @Mapping(target = "creatorName", expression = "java(course.getCreator().getName() + \" \" + course.getCreator().getSurname())")
  CourseInfo map(Course course);

  @Named("getGroupsId")
  default List<UUID> getGroupsId(Set<Group> groups) {
    return groups.stream()
        .filter(group -> !group.isDeleted())
        .map(Group::getId).toList();
  }

  @Mapping(target = "creatorName", expression = "java(course.getCreator().getName() + \" \" + course.getCreator().getSurname())")
  ResponseCourse mapResponseCourse(Course course);

  List<ResponseCourse> mapResponseCourse(List<Course> course);

  ResponseCourseInfo mapResponseCourseInfo(Course course);

}

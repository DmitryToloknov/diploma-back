package org.diploma.user.controller.skill;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.diploma.user.controller.course.dto.CourseProgress;
import org.diploma.user.controller.skill.dto.CreateSkillDto;
import org.diploma.user.controller.skill.dto.ResponseSkill;
import org.diploma.user.controller.skill.dto.SkillUser;
import org.diploma.user.service.SkillService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("skill")
public class SkillController {

  private final SkillService skillService;

  @PostMapping
  @PreAuthorize("hasRole('COURSE_UPDATE') || hasRole('ADMIN')")
  public ResponseEntity createSkill (@RequestBody  @Valid CreateSkillDto createSkillDto, Authentication authentication) {
    skillService.createSkill(createSkillDto, UUID.fromString(authentication.getName()));
    return ResponseEntity.ok().build();
  }

  @GetMapping
  @PreAuthorize("hasRole('COURSE_UPDATE') || hasRole('ADMIN')")
  public ResponseEntity<List<ResponseSkill>> getSkills() {
    return ResponseEntity.ok(skillService.getSkills());
  }

  @GetMapping("/user")
  public ResponseEntity<List<SkillUser>> getCourseProcesses(Authentication authentication) {
    return ResponseEntity.ok(skillService.getUserSkills(UUID.fromString(authentication.getName())));
  }

}

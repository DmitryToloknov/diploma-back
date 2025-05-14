package org.diploma.user.controller.news;

import lombok.RequiredArgsConstructor;
import org.diploma.user.controller.news.dto.NewsPreviewResponse;
import org.diploma.user.controller.news.dto.NewsRequest;
import org.diploma.user.controller.news.dto.NewsResponse;
import org.diploma.user.service.NewsService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("news")
public class NewsController {

  private final NewsService newsService;

  @PostMapping
  @PreAuthorize("hasRole('NEWS_UPDATE') || hasRole('ADMIN')")
  public ResponseEntity<UUID> createNews(Authentication authentication) {
    return ResponseEntity.ok(newsService.createNews(UUID.fromString(authentication.getName())));
  }

  @GetMapping("/{id}/edit")
  @PreAuthorize("hasRole('NEWS_UPDATE') || hasRole('ADMIN')")
  public ResponseEntity<NewsResponse> findByIdEdit(@PathVariable UUID id) {
    return ResponseEntity.ok(newsService.findById(id));
  }

  @GetMapping("/{id}")
  public ResponseEntity<NewsPreviewResponse> findById(@PathVariable UUID id, Authentication authentication) {
    return ResponseEntity.ok(newsService.findById(id, authentication));
  }

  @GetMapping()
  public ResponseEntity<List<NewsPreviewResponse>> find(@RequestParam int page, @RequestParam int perPage,
                                                           Authentication authentication) {
    return ResponseEntity.ok(newsService.findAll(authentication, page, perPage));
  }

  @GetMapping("/count")
  public ResponseEntity<Integer> count(Authentication authentication) {
    return ResponseEntity.ok(newsService.count(authentication));
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('NEWS_UPDATE') || hasRole('ADMIN')")
  public ResponseEntity deleteById(@PathVariable UUID id) {
    newsService.deleteById(id);
    return ResponseEntity.ok().build();
  }

  @PutMapping("/{id}")
  @PreAuthorize("hasRole('NEWS_UPDATE') || hasRole('ADMIN')")
  public ResponseEntity updateNews(@RequestBody NewsRequest newsRequest, @PathVariable UUID id) {
    newsService.updateById(newsRequest, id);
    return ResponseEntity.ok().build();
  }

}

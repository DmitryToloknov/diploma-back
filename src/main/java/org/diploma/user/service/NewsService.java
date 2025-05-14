package org.diploma.user.service;

import lombok.RequiredArgsConstructor;
import org.diploma.user.Entity.News;
import org.diploma.user.mapper.NewsMapper;
import org.diploma.user.controller.news.dto.NewsPreviewResponse;
import org.diploma.user.controller.news.dto.NewsRequest;
import org.diploma.user.controller.news.dto.NewsResponse;
import org.diploma.user.exception.CustomException;
import org.diploma.user.repository.NewsRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.diploma.user.exception.ExceptionMessageConstants.NEWS_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class NewsService {

  private static final String DEFAULT_NAME = "Новая новость!";

  private final NewsRepository newsRepository;
  private final NewsMapper newsMapper;
  private final UserService userService;

  public UUID createNews(UUID creatorId) {
    var news = new News();
    news.setCreatorId(creatorId);
    news.setName(DEFAULT_NAME);
    newsRepository.save(news);
    return news.getId();
  }

  @Transactional
  public NewsResponse findById(UUID id) {
    var newsResponse = newsMapper.toNewsResponse(
        newsRepository.findByIdAndIsDeletedFalse(id).orElseThrow(() -> new CustomException(NEWS_NOT_FOUND)));
    userService.findById(newsResponse.getCreatorId()).ifPresent(
        user -> newsResponse.setFullNameCreator(String.format("%s %s", user.getName(), user.getSurname())));
    return newsResponse;
  }

  public List<NewsPreviewResponse> findAll(Authentication authentication, int page, int perPage) {
    boolean hasEditRole = authentication != null &&
        authentication.getAuthorities().stream()
            .anyMatch(a -> a.getAuthority().equals("ROLE_NEWS_UPDATE") || a.getAuthority().equals("ROLE_ADMIN"));
    var newsList = newsRepository.findAllFiltered(hasEditRole, perPage, page * perPage);
    return newsList.stream()
        .map(news -> {
          String trimmedDescription = trimDescription(news.getDescription(), 5); // Обрезаем до 3 абзацев
          news.setDescription(trimmedDescription);
          return news;
        })
        .collect(Collectors.toList());
  }

  public int count(Authentication authentication) {
    boolean hasEditRole = authentication != null &&
        authentication.getAuthorities().stream()
            .anyMatch(a -> a.getAuthority().equals("ROLE_NEWS_UPDATE") || a.getAuthority().equals("ROLE_ADMIN"));
    return newsRepository.count(hasEditRole);
  }

  @Transactional
  public NewsPreviewResponse findById(UUID id, Authentication authentication) {
    var news = newsRepository.findByIdAndIsDeletedFalse(id).orElseThrow(() -> new CustomException(NEWS_NOT_FOUND));

    boolean hasUpdateRole = authentication != null &&
        authentication.getAuthorities().stream()
            .anyMatch(a -> a.getAuthority().equals("ROLE_NEWS_UPDATE") || a.getAuthority().equals("ROLE_ADMIN"));

    if (!news.isStatus() && !hasUpdateRole) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN);
    }

    var newsResponse = newsMapper.toNewsPreviewResponse(news);
    userService.findById(newsResponse.getCreatorId()).ifPresent(
        user -> newsResponse.setFullNameCreator(String.format("%s %s", user.getName(), user.getSurname())));
    return newsResponse;
  }

  public void deleteById(UUID id) {
    newsRepository.markAsDeleted(id);
  }

  @Transactional
  public void updateById(NewsRequest newsRequest, UUID id) {
    var news = newsRepository.findById(id).orElseThrow(() -> new CustomException(NEWS_NOT_FOUND));
    if(newsRequest.isStatus() && !news.isStatus()) {
      news.setDatePublication(LocalDateTime.now());
    } else if(!newsRequest.isStatus() && news.isStatus()) {
      news.setDatePublication(null);
    }
    news.setName(newsRequest.getName());
    news.setDescription(newsRequest.getDescription());
    news.setStatus(newsRequest.isStatus());
    newsRepository.save(news);
  }

  private String trimDescription(String description, int maxParagraphs) {
    if (description == null || description.isBlank()) {
      return null;
    }

    // Разбиваем по двойному переводу строки (игнорируя пробелы)
    String[] paragraphs = description.split("\n\\s*\n");

    // Оставляем только первые maxParagraphs абзацев и соединяем обратно
    return Arrays.stream(paragraphs)
        .limit(maxParagraphs)
        .collect(Collectors.joining("\n\n"));
  }
}

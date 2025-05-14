package org.diploma.user.mapper;

import org.diploma.user.Entity.News;
import org.diploma.user.controller.news.dto.NewsPreviewResponse;
import org.diploma.user.controller.news.dto.NewsResponse;
import org.mapstruct.Mapper;

@Mapper
public interface NewsMapper {
  News toEntity(NewsResponse newsResponse);

  NewsResponse toNewsResponse(News news);
  NewsPreviewResponse toNewsPreviewResponse(News news);
}
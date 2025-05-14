package org.diploma.user.controller.news.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewsRequest {
    private String name;
    private String description;
    private boolean isStatus;
}

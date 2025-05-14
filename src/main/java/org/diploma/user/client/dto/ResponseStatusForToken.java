package org.diploma.user.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseStatusForToken {
  @JsonProperty("active")
  private Boolean status;
}

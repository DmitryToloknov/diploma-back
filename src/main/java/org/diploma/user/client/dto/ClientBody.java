package org.diploma.user.client.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientBody {
  private String grantType;
  private String clientId;
  private String clientSecret;
}

package org.diploma.user.client.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CredentialKeycloak {
  private final String type = "password";
  private String value;
  private final boolean temporary = false;
}

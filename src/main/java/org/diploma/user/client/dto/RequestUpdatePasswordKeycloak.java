package org.diploma.user.client.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RequestUpdatePasswordKeycloak {
  private List<CredentialKeycloak> credentials;
}

package org.diploma.user.client.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestRegisterKeycloak {

  private String username;
  private boolean enabled = true;
  private List<CredentialKeycloak> credentials;

}

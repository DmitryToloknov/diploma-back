package org.diploma.user.config.keycloak;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "keycloak")
public class KeycloakProperties {

  /**
   * Id клиента из keycloak.
   */
  private String clientId;
  /**
   * Секрет клиента из keycloak.
   */
  private String clientSecret;
  /**
   * Хост keycloak. Например http://localhost:8081/realms/diploma.
   */
  private String host;

}

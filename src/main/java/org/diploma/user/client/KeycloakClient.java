package org.diploma.user.client;

import java.util.Map;
import org.diploma.user.client.dto.ResponseStatusForToken;
import org.diploma.user.client.dto.ResponseToken;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "keycloak", url = "${keycloak.host}")
public interface KeycloakClient {

  @PostMapping(value = "/protocol/openid-connect/revoke", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  ResponseToken revoke(Map<String, ?> formData);

  @PostMapping(value = "/protocol/openid-connect/token", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  ResponseToken token(Map<String, ?> formData);

  @PostMapping(value = "/protocol/openid-connect/token/introspect", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  ResponseStatusForToken validate(Map<String, ?> formData);

}

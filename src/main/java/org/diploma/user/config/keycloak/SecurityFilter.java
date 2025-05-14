package org.diploma.user.config.keycloak;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class SecurityFilter extends OncePerRequestFilter {

  private static final String BEARER = "Bearer ";
  private static final String URL = "/protocol/openid-connect/token/introspect";
  private final KeycloakProperties keycloakProperties;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
    throws ServletException, IOException {
    final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
    if (header != null && header.startsWith(BEARER)) {
      String token = header.substring(7);

      if (!isTokenValid(token)) {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        return;
      }
    }

    filterChain.doFilter(request, response);
  }

  private boolean isTokenValid(String token) {
    try {
      var headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

      var body = new org.springframework.util.LinkedMultiValueMap<String, String>();
      body.add("token", token);
      body.add("client_id", keycloakProperties.getClientId());
      body.add("client_secret", keycloakProperties.getClientSecret());

      var requestEntity = new HttpEntity<>(body, headers);
      var restTemplate = new RestTemplate();
      var responseEntity = restTemplate.postForEntity(keycloakProperties.getHost() + URL, requestEntity, String.class);

      var objectMapper = new ObjectMapper();
      if (responseEntity.getStatusCode().is2xxSuccessful()) {
        JsonNode jsonNode = objectMapper.readTree(responseEntity.getBody());
        return jsonNode.get("active").asBoolean();
      }
    } catch (Exception e) {
      log.error("Error isTokenValid", e);
    }
    return false;
  }
}

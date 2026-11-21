package com.campus.events.controller;

import com.campus.events.model.User;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class MeController {
  @GetMapping("/api/me")
  public Map<String, Object> me(@AuthenticationPrincipal User user) {
    if (user == null) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authentication required");
    }
    return Map.of("user", Map.of(
      "id", user.getId(),
      "username", user.getUsername(),
      "email", user.getEmail(),
      "role", user.getRole()
    ));
  }
}

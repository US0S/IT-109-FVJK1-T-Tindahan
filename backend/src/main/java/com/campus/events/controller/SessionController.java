package com.campus.events.controller;

import com.campus.events.model.Session;
import com.campus.events.repository.SessionRepository;
import java.util.UUID;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sessions")
@PreAuthorize("hasRole('SUPER_ADMIN')")
public class SessionController {
  private final SessionRepository sessions;

  public SessionController(SessionRepository sessions) {
    this.sessions = sessions;
  }

  @GetMapping
  public java.util.List<Session> findAll() {
    return sessions.findAll();
  }

  @DeleteMapping("/{id}")
  public void delete(@PathVariable UUID id) {
    sessions.deleteById(id);
  }
}

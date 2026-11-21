package com.campus.events.controller;

import com.campus.events.model.User;
import com.campus.events.repository.UserRepository;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/users")
@PreAuthorize("hasRole('SUPER_ADMIN')")
public class UserController {
  private final UserRepository users;

  public UserController(UserRepository users) {
    this.users = users;
  }

  @GetMapping
  public java.util.List<User> findAll() {
    return users.findAll();
  }

  @GetMapping("/{id}")
  public User findOne(@PathVariable UUID id) {
    return users.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
  }

  @DeleteMapping("/{id}")
  public void delete(@PathVariable UUID id) {
    users.deleteById(id);
  }
}

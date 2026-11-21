package com.campus.events.controller;

import com.campus.events.dto.AuthDtos;
import com.campus.events.model.User;
import com.campus.events.repository.UserRepository;
import com.campus.events.security.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
  private final UserRepository users;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;

  public AuthController(UserRepository users, PasswordEncoder passwordEncoder, JwtService jwtService) {
    this.users = users;
    this.passwordEncoder = passwordEncoder;
    this.jwtService = jwtService;
  }

  @PostMapping("/register")
  public java.util.Map<String, Object> register(@Valid @RequestBody AuthDtos.RegisterRequest request) {
    String username = request.username().toLowerCase();
    if (users.findByUsername(username).isPresent()) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, "Username is already registered");
    }
    String email = request.email().trim().toLowerCase();
    if (users.findByEmail(email).isPresent()) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, "Email is already registered");
    }
    if (request.role() == com.campus.events.model.Role.SUPER_ADMIN) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Super Admin registration is not allowed");
    }

    User user = new User();
    user.setUsername(username);
    user.setEmail(email);
    user.setPassword(passwordEncoder.encode(request.password()));
    user.setRole(request.role());
    user = users.save(user);
    return java.util.Map.of(
      "message", "Account created successfully",
      "user", new AuthDtos.UserResponse(user.getId(), user.getUsername(), user.getEmail(), user.getRole())
    );
  }

  @PostMapping("/login")
  public AuthDtos.AuthResponse login(@Valid @RequestBody AuthDtos.LoginRequest request, HttpServletResponse response) {
    User user = users.findByEmail(request.email().trim().toLowerCase())
      .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password"));
    if (!user.isActive() || !passwordEncoder.matches(request.password(), user.getPassword())) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password");
    }
    return issue(user, response);
  }

  @PostMapping("/logout")
  public java.util.Map<String, Boolean> logout(HttpServletResponse response) {
    Cookie cookie = new Cookie("campus_session", "");
    cookie.setHttpOnly(true);
    cookie.setPath("/");
    cookie.setMaxAge(0);
    response.addCookie(cookie);
    return java.util.Map.of("success", true);
  }

  @GetMapping("/me")
  public java.util.Map<String, Object> me(@AuthenticationPrincipal User user) {
    if (user == null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authentication required");
    return java.util.Map.of("user", new AuthDtos.UserResponse(user.getId(), user.getUsername(), user.getEmail(), user.getRole()));
  }

  private AuthDtos.AuthResponse issue(User user, HttpServletResponse response) {
    String token = jwtService.generate(user);
    Cookie cookie = new Cookie("campus_session", token);
    cookie.setHttpOnly(true);
    cookie.setPath("/");
    cookie.setMaxAge(7 * 24 * 60 * 60);
    response.addCookie(cookie);
    return new AuthDtos.AuthResponse(token, new AuthDtos.UserResponse(user.getId(), user.getUsername(), user.getEmail(), user.getRole()));
  }
}

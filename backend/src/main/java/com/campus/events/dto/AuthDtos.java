package com.campus.events.dto;

import com.campus.events.model.Role;
import jakarta.validation.constraints.Pattern;

public class AuthDtos {
  public record RegisterRequest(
    @Pattern(regexp = "^[A-Za-z0-9_]{3,20}$", message = "Invalid username") String username,
    @Pattern(regexp = "^[A-Za-z0-9.!#$%&'*+/=?^_`{|}~-]+@[A-Za-z0-9-]+(?:\\.[A-Za-z0-9-]+)*$", message = "Invalid email") String email,
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d@$!%*?&#]{8,64}$", message = "Invalid password") String password,
    Role role
  ) {}

  public record LoginRequest(
    @Pattern(regexp = "^[A-Za-z0-9.!#$%&'*+/=?^_`{|}~-]+@[A-Za-z0-9-]+(?:\\.[A-Za-z0-9-]+)*$", message = "Invalid email") String email,
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d@$!%*?&#]{8,64}$", message = "Invalid password") String password
  ) {}

  public record AuthResponse(String accessToken, UserResponse user) {}
  public record UserResponse(java.util.UUID id, String username, String email, Role role) {}
}

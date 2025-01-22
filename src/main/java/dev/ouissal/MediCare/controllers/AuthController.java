package dev.ouissal.MediCare.controllers;

import dev.ouissal.MediCare.DTOs.LoginRequest;
import dev.ouissal.MediCare.DTOs.RegisterRequest;
import dev.ouissal.MediCare.DTOs.UpdateUserRequest;
import dev.ouissal.MediCare.jwt.util.JwtUtil;
import dev.ouissal.MediCare.services.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest, HttpServletResponse response) {
        try {
            String token = authService.register(registerRequest);
            // Add token to cookie
            Cookie cookie = new Cookie("JWT", token);
            cookie.setHttpOnly(true); // Secure against XSS
            cookie.setPath("/"); // Make it available to all endpoints
            cookie.setMaxAge(60 * 60 * 10); // Expire after 10 hours
            response.addCookie(cookie);
            return ResponseEntity.ok(Map.of(
                    "message", "User successfully registered!",
                    "token", token
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "error", e.getMessage()
            ));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        try {
            String token = authService.login(loginRequest);

            Cookie cookie = new Cookie("JWT", token);
            cookie.setHttpOnly(true); // Secure against XSS
            cookie.setPath("/"); // Make it available to all endpoints
            cookie.setMaxAge(60 * 60 * 10); // Expire after 10 hours
            response.addCookie(cookie);

            return ResponseEntity.ok(Map.of(
                    "message", "User successfully logged in!",
                    "token", token
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "error", e.getMessage()
            ));
        }
    }


}



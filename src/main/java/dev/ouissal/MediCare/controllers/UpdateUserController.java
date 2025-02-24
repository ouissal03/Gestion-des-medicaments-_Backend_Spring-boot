package dev.ouissal.MediCare.controllers;

import dev.ouissal.MediCare.DTOs.UpdateUserRequest;
import dev.ouissal.MediCare.models.Notification;
import dev.ouissal.MediCare.models.User;
import dev.ouissal.MediCare.services.AuthService;
import dev.ouissal.MediCare.services.UpdateUserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
@RestController
@RequestMapping("/api/update")
public class UpdateUserController {
    @Autowired
    private UpdateUserService updateUserService;

    @GetMapping("/user")
    public ResponseEntity<?> getUser(HttpServletRequest request) {
        try {
            User user = updateUserService.getUserById(request);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "error", e.getMessage()
            ));
        }
    }

    @PutMapping
    public ResponseEntity<?> updateUser(@Valid @RequestBody UpdateUserRequest updateUserRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName(); // Get email from authenticated user
        System.out.println("Authenticated email: " + email);

        if (updateUserRequest.hasUnknownFields()) {
            return ResponseEntity.badRequest().body(Map.of(
                    "error", "Invalid field(s): " + updateUserRequest.getUnknownFields().keySet()
            ));
        }

        try {
            updateUserService.updateUser(email, updateUserRequest);
            return ResponseEntity.ok(Map.of(
                    "message", "User information updated successfully!"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "error", "Error updating user information: " + e.getMessage()
            ));
        }
    }


    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity.badRequest().body(Map.of(
                "error", ex.getMessage()
        ));
    }
}

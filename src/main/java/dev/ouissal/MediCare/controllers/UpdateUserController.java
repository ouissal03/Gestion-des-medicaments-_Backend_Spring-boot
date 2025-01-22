package dev.ouissal.MediCare.controllers;

import dev.ouissal.MediCare.DTOs.UpdateUserRequest;
import dev.ouissal.MediCare.services.AuthService;
import dev.ouissal.MediCare.services.UpdateUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
@RestController
@RequestMapping("/api/update")
public class UpdateUserController {
    @Autowired
    private UpdateUserService updateUserService;

    @PutMapping
    public ResponseEntity<?> updateUser(@RequestBody UpdateUserRequest updateUserRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName(); // Get email from authenticated user
        System.out.println("Authenticated email: " + email);

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

package dev.ouissal.MediCare.services;

import dev.ouissal.MediCare.DTOs.LoginRequest;
import dev.ouissal.MediCare.DTOs.RegisterRequest;
import dev.ouissal.MediCare.models.Pillbox;
import dev.ouissal.MediCare.models.User;
import dev.ouissal.MediCare.repositories.PillboxRepository;
import dev.ouissal.MediCare.repositories.UserRepository;
import dev.ouissal.MediCare.jwt.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PillboxRepository pillboxRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    private JwtUtil jwtUtil;

    public String register(RegisterRequest registerRequest) {
        // Check if email already exists
        if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            throw new RuntimeException("Email already in use!");
        }

        // Check if pillbox is available
        Pillbox pillbox = pillboxRepository.findByIdAndAssignedFalse(registerRequest.getPillboxId());
        if (pillbox == null) {
            throw new RuntimeException("Invalid or already assigned Pillbox ID!");
        }

        // Save user
        User user = new User();
        user.setFirstName(registerRequest.getFirstName());
        user.setLastName(registerRequest.getLastName());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setPillboxId(registerRequest.getPillboxId());
        user.setCreatedAt(Instant.now());
        user.setUpdatedAt(Instant.now());
        User savedUser = userRepository.save(user);

        // Mark pillbox as assigned
        pillbox.setAssigned(true);
        pillbox.setAssignedTo(savedUser.getId());
        pillbox.setUpdatedAt(Instant.now());
        pillboxRepository.save(pillbox);

        // Generate JWT token
        return jwtUtil.generateToken(savedUser.getEmail(), savedUser.getId());

    }


    public String login(LoginRequest loginRequest) {
        Optional<User> optionalUser = userRepository.findByEmail(loginRequest.getEmail());
        if (optionalUser.isEmpty()) {
            throw new IllegalArgumentException("Error: Invalid email or password.");
        }

        User existingUser = optionalUser.get();
        if (!passwordEncoder.matches(loginRequest.getPassword(), existingUser.getPassword())) {
            throw new IllegalArgumentException("Error: Invalid email or password.");
        }

        return jwtUtil.generateToken(existingUser.getEmail(), existingUser.getId());

    }


}

package dev.ouissal.MediCare.services;

import dev.ouissal.MediCare.DTOs.UpdateUserRequest;
import dev.ouissal.MediCare.models.User;
import dev.ouissal.MediCare.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class UpdateUserService {

    @Autowired
    private UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    public void updateUser(String email, UpdateUserRequest updateUserRequest) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found!"));

        // Update the user information if provided
        if (updateUserRequest.getFirstName() != null) {
            user.setFirstName(updateUserRequest.getFirstName());
        }

        if (updateUserRequest.getLastName() != null) {
            user.setLastName(updateUserRequest.getLastName());
        }

        if (updateUserRequest.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(updateUserRequest.getPassword()));
        }

        if (updateUserRequest.getEmail() != null) {
            user.setEmail(updateUserRequest.getEmail());
        }

        if (updateUserRequest.getImage() != null) {
            user.setImage(updateUserRequest.getImage());
        }

        // Update the timestamp
        user.setUpdatedAt(Instant.now());

        // Save the updated user to the database
        userRepository.save(user);
    }

}

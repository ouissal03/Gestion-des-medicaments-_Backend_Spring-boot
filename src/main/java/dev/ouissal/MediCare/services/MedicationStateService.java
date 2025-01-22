package dev.ouissal.MediCare.services;

import dev.ouissal.MediCare.jwt.util.JwtUtil;
import dev.ouissal.MediCare.models.MedicationState;
import dev.ouissal.MediCare.models.Pillbox;
import dev.ouissal.MediCare.repositories.MedicationStateRepository;
import dev.ouissal.MediCare.repositories.PillboxRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

@Service
public class MedicationStateService {

    @Autowired
    private PillboxRepository pillboxRepository;

    @Autowired
    private MedicationStateRepository medicationStateRepository;

    @Autowired
    private JwtUtil jwtUtil;

    public Map<String, String> getMedicationStatesForToday(HttpServletRequest request) {
        // Extract JWT token from cookies
        String token = extractTokenFromCookies(request);
        if (token == null || !jwtUtil.isTokenValid(token)) {
            throw new RuntimeException("Invalid or missing authentication token.");
        }

        // Extract user ID (email) from the token
        String userId = jwtUtil.extractUserId(token);

        // Fetch the pillbox assigned to the user
        Optional<Pillbox> optionalPillbox = pillboxRepository.findByAssignedTo(userId);
        if (optionalPillbox.isEmpty()) {
            throw new RuntimeException("No pillbox assigned to the user.");
        }

        Pillbox pillbox = optionalPillbox.get();

        // Fetch today's date in ISO format (e.g., "2025-01-22")
        String todayDate = LocalDate.now().toString();

        // Fetch the medication state for the pillbox and today's date
        Optional<MedicationState> optionalState =
                medicationStateRepository.findByPillboxIdAndDate(pillbox.getId(), todayDate);

        if (optionalState.isEmpty()) {
            throw new RuntimeException("No medication state found for today.");
        }

        MedicationState medicationState = optionalState.get();

        // Return the intakeStatus (breakfast, lunch, dinner)
        return medicationState.getIntakeStatus();
    }

    public Map<String, String> getMedicationStatesArchive(HttpServletRequest request, String date) {
        // Extract JWT token from cookies
        String token = extractTokenFromCookies(request);
        if (token == null || !jwtUtil.isTokenValid(token)) {
            throw new RuntimeException("Invalid or missing authentication token.");
        }

        // Extract user ID (email) from the token
        String userId = jwtUtil.extractUserId(token);

        // Fetch the pillbox assigned to the user
        Optional<Pillbox> optionalPillbox = pillboxRepository.findByAssignedTo(userId);
        if (optionalPillbox.isEmpty()) {
            throw new RuntimeException("No pillbox assigned to the user.");
        }

        Pillbox pillbox = optionalPillbox.get();

        // Fetch the medication state for the pillbox and the provided date
        Optional<MedicationState> optionalState =
                medicationStateRepository.findByPillboxIdAndDate(pillbox.getId(), date);

        if (optionalState.isEmpty()) {
            throw new RuntimeException("No medication state found for the given date.");
        }

        MedicationState medicationState = optionalState.get();

        // Return the intakeStatus (breakfast, lunch, dinner)
        return medicationState.getIntakeStatus();
    }

    private String extractTokenFromCookies(HttpServletRequest request) {
        if (request.getCookies() != null) {
            for (var cookie : request.getCookies()) {
                if ("JWT".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    public MedicationState createOrUpdateMedicationState(MedicationState medicationStateRequest) {
        // Check if medication state for the given pillboxId and date already exists
        Optional<MedicationState> existingMedicationState = medicationStateRepository
                .findByPillboxIdAndDate(medicationStateRequest.getPillboxId(), medicationStateRequest.getDate());

        MedicationState medicationState;
        if (existingMedicationState.isPresent()) {
            // If exists, update the existing record
            medicationState = existingMedicationState.get();
            medicationState.setIntakeStatus(medicationStateRequest.getIntakeStatus());  // Update intakeStatus
            medicationState.setUpdatedAt(Instant.now());  // Update the updatedAt field
        } else {
            // If does not exist, create a new record
            medicationState = medicationStateRequest;
        }

        // Save the medication state (either newly created or updated)
        return medicationStateRepository.save(medicationState);
    }
}

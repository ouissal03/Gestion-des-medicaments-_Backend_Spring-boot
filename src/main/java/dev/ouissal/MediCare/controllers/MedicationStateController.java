package dev.ouissal.MediCare.controllers;

import dev.ouissal.MediCare.models.MedicationState;
import dev.ouissal.MediCare.services.MedicationStateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

import java.time.Instant;
import java.util.Map;

@RestController
@RequestMapping("/api/medication")
public class MedicationStateController {

    @Autowired
    private MedicationStateService medicationStateService;

    // Endpoint to get medication states for today
    @GetMapping("/today")
    public ResponseEntity<?> getMedicationStatesForToday(HttpServletRequest request) {
        try {
            Map<String, String> medicationStates = medicationStateService.getMedicationStatesForToday(request);
            return ResponseEntity.ok(medicationStates);
        } catch (RuntimeException ex) {
            // Handle errors and return error message in response body
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", ex.getMessage()));
        } catch (Exception ex) {
            // Handle unexpected errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An unexpected error occurred."));
        }
    }

    // Endpoint to get medication states for a specific date entered in the request body
    @PostMapping("/archive")
    public ResponseEntity<?> getMedicationStatesArchive(
            HttpServletRequest request,
            @RequestBody Map<String, String> dateRequest) {
        try {
            String date = dateRequest.get("date");  // Extract the date from the body
            Map<String, String> medicationStates = medicationStateService.getMedicationStatesArchive(request, date);
            return ResponseEntity.ok(medicationStates);
        } catch (RuntimeException ex) {
            // Handle errors and return error message in response body
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", ex.getMessage()));
        } catch (Exception ex) {
            // Handle unexpected errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An unexpected error occurred."));
        }
    }

    @PostMapping("/createOrEdit")
    public ResponseEntity<?> createOrUpdateMedicationState(@RequestBody MedicationState medicationStateRequest) {
        try {
            // Set createdAt and updatedAt fields automatically
            Instant now = Instant.now();
            medicationStateRequest.setCreatedAt(now);
            medicationStateRequest.setUpdatedAt(now);

            // Check if the pillboxId and date already exist in the collection
            MedicationState medicationState = medicationStateService.createOrUpdateMedicationState(medicationStateRequest);

            return ResponseEntity.status(HttpStatus.CREATED).body(medicationState);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", ex.getMessage()));
        }
    }
}

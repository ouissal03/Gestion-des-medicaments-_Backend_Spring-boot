package dev.ouissal.MediCare.models;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.Map;
@Data
@Document(collection = "medication_states")
public class MedicationState {

    @Id
    private String id;
    private String pillboxId;
    private String date;
    private Map<String, String> intakeStatus; // Keys: breakfast, lunch, dinner

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;
}

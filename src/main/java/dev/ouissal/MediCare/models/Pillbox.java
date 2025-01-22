package dev.ouissal.MediCare.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "pillboxes")
public class Pillbox {
    @Id
    private String id;
    private boolean assigned;
    private String assignedTo;
    private Instant createdAt;
    private Instant updatedAt;
}

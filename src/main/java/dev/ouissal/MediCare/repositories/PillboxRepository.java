package dev.ouissal.MediCare.repositories;

import dev.ouissal.MediCare.models.Pillbox;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface PillboxRepository extends MongoRepository<Pillbox, String> {
    // For registration
    Pillbox findByIdAndAssignedFalse(String id);

    // For medication state service
    Optional<Pillbox> findByAssignedTo(String assignedTo);
}

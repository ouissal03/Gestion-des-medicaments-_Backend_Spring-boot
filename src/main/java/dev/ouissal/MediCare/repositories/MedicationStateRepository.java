package dev.ouissal.MediCare.repositories;


import dev.ouissal.MediCare.models.MedicationState;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MedicationStateRepository extends MongoRepository<MedicationState, String> {
    Optional<MedicationState> findByPillboxIdAndDate(String pillboxId, String date);
}

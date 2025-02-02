package dev.ouissal.MediCare.services;

import dev.ouissal.MediCare.models.MedicationState;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class MedicationStatusListener {

    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;

    public MedicationStatusListener(ObjectMapper objectMapper, RestTemplate restTemplate) {
        this.objectMapper = objectMapper;
        this.restTemplate = restTemplate;
    }

    @RabbitListener(queues = "pillbox/medication_status")
    public void receiveMessage(String message) {
        try {
            MedicationState medicationState = objectMapper.readValue(message, MedicationState.class);
            System.out.println("Message reçu depuis RabbitMQ: " + medicationState);

            // Envoyer les données reçues à l'API backend
            String backendUrl = "http://localhost:8080/api/medication/createOrEdit"; // Remplace avec l'URL correcte
            ResponseEntity<String> response = restTemplate.postForEntity(backendUrl, medicationState, String.class);

            if (response.getStatusCode() == HttpStatus.CREATED) {
                System.out.println("Données envoyées avec succès à l'API !");
            } else {
                System.out.println("Erreur lors de l'envoi des données à l'API: " + response.getBody());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

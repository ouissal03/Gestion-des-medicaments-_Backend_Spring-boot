package dev.ouissal.MediCare.services;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;

@Service
public class NotificationListener {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public NotificationListener(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    @RabbitListener(queues = "pillbox/notifications")
    public void receiveNotification(String message) {
        try {
            // Convert JSON string to a Map
            Map<String, String> notificationData = objectMapper.readValue(message, Map.class);

            // Forward the notification to the API endpoint
            String apiUrl = "http://localhost:8080/api/notifications/create";
            ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, notificationData, String.class);

            // Log the response
            System.out.println("Notification forwarded to API. Response: " + response.getBody());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

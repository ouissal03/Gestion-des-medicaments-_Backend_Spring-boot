package dev.ouissal.MediCare.controllers;

import dev.ouissal.MediCare.DTOs.NotificationRequest;
import dev.ouissal.MediCare.models.Pillbox;
import dev.ouissal.MediCare.repositories.PillboxRepository;
import dev.ouissal.MediCare.services.NotificationService;
import dev.ouissal.MediCare.models.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;
    @Autowired
    private PillboxRepository pillboxRepository;

    // Endpoint to get notifications for the authenticated user
    @GetMapping("/getAll")
    public ResponseEntity<?> getNotifications(HttpServletRequest request) {
        List<Notification> notifications = notificationService.getNotificationsForUser(request);

        if (notifications == null || notifications.isEmpty()) {
            return ResponseEntity.ok("No notifications for the authenticated user.");
        }

        return ResponseEntity.ok(notifications);
    }

    // POST endpoint to insert notifications
    @PostMapping("/create")
    public ResponseEntity<String> createNotification(@RequestBody NotificationRequest notificationRequest) {
        // Fetch the pillbox by ID
        Pillbox pillbox = pillboxRepository.findById(notificationRequest.getPillboxId()).orElse(null);

        if (pillbox == null) {
            return ResponseEntity.status(400).body("Pillbox ID not found.");
        }

        String userId = pillbox.getAssignedTo();

        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setMessage(notificationRequest.getMessage());
        notification.setCreatedAt(Instant.now());
        notification.setIsRead(false);

        notificationService.save(notification);

        return ResponseEntity.ok("Notification created successfully.");
    }

    // Endpoint to mark a notification as read
    @PatchMapping("/markAsRead/{id}")
    public ResponseEntity<String> markNotificationAsRead(@PathVariable String id) {
        boolean isUpdated = notificationService.markAsRead(id);

        if (isUpdated) {
            return ResponseEntity.ok("Notification marked as read successfully.");
        } else {
            return ResponseEntity.status(404).body("Notification not found.");
        }
    }
}
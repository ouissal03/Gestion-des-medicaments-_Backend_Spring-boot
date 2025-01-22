package dev.ouissal.MediCare.services;

import dev.ouissal.MediCare.models.Notification;
import dev.ouissal.MediCare.repositories.NotificationRepository;
import dev.ouissal.MediCare.jwt.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private JwtUtil jwtUtil;

    // Get notifications for the authenticated user
    public List<Notification> getNotificationsForUser(HttpServletRequest request) {
        // Extract JWT token from cookies
        String token = extractTokenFromCookies(request);
        if (token == null || !jwtUtil.isTokenValid(token)) {
            throw new RuntimeException("Invalid or missing authentication token.");
        }

        // Extract user ID from the token
        String userId = jwtUtil.extractUserId(token);

        // Retrieve notifications for the user
        return notificationRepository.findByUserId(userId);
    }

    // Helper method to extract token from cookies
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

    // Method to save the notification
    public void save(Notification notification) {
        notificationRepository.save(notification);
    }

    public boolean markAsRead(String id) {
        Optional<Notification> optionalNotification = notificationRepository.findById(id);

        if (optionalNotification.isPresent()) {
            Notification notification = optionalNotification.get();
            notification.setIsRead(true);  // Set isRead to true
            notificationRepository.save(notification);  // Save the updated notification
            return true;
        }
        return false;  // Notification not found
    }
}

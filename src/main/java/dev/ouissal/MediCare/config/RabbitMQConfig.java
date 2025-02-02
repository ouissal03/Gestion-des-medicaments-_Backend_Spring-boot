package dev.ouissal.MediCare.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public Queue medicationQueue() {
        return new Queue("medication_status", true);
    }
    @Bean
    public Queue notificationQueue() {
        return new Queue("pillbox/notifications", true);
    }
}
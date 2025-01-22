package dev.ouissal.MediCare.DTOs;

import lombok.Data;

@Data
public class NotificationRequest {

    private String pillboxId;
    private String message;

}


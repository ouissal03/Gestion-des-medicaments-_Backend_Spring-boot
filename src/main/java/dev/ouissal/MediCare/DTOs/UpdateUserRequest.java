package dev.ouissal.MediCare.DTOs;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = false) // Reject unknown fields
public class UpdateUserRequest {

    @JsonProperty("firstName")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    private String firstName;

    @JsonProperty("lastName")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    private String lastName;

    @JsonProperty("password")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String password;

    @JsonProperty("email")
    @Email(message = "Invalid email format")
    private String email;

    @JsonProperty("image")
    private String image;
}

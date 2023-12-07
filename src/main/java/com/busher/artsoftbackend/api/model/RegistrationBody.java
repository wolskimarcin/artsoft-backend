package com.busher.artsoftbackend.api.model;

import jakarta.validation.constraints.*;

public class RegistrationBody {

    @NotNull
    @NotBlank
    @Size(min = 3, max = 255)
    private String username;
    @NotNull
    @NotBlank
    @Email
    private String email;
    @NotNull
    @NotBlank
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d).{6,}$")
    @Size(min = 6, max = 32)
    private String password;
    @NotNull
    @NotBlank
    private String firstName;
    @NotNull
    @NotBlank
    private String lastName;

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
}
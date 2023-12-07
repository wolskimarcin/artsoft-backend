package com.busher.artsoftbackend.api.model;


/**
 * The information required to register a user.
 */
public class RegistrationBody {

    private String username;
    private String email;
    private String password;
    private String firstName;
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
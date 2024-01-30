package com.busher.artsoftbackend.exception;

public class UserNotVerifiedException extends Exception {

    private final boolean isNewEmailSent;

    public UserNotVerifiedException(boolean isNewEmailSent) {
        this.isNewEmailSent = isNewEmailSent;
    }
    public boolean isNewEmailSent() {
        return isNewEmailSent;
    }

}
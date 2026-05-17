package com.digitalid.exception;

public class IdentityNotFoundException extends RuntimeException {

    public IdentityNotFoundException(String message) {
        super(message); // messaeg= descrip of which identity was not found
    }

    public static IdentityNotFoundException forIdNumber(String digitalIdNumber) {
        return new IdentityNotFoundException(
                "Digital ID not found: " + digitalIdNumber);
    }
}

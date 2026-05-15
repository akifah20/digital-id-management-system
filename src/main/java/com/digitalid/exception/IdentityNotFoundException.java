package com.digitalid.exception;

//Thrown when an ID is looked up by ID number does NOT exist in the system.

public class IdentityNotFoundException extends RuntimeException {

    public IdentityNotFoundException(String message) {
        super(message); // messaeg= descrip of which identity was not found
    }

    public static IdentityNotFoundException forIdNumber(String digitalIdNumber) {
        return new IdentityNotFoundException(
                "Digital ID not found: " + digitalIdNumber);
    }
}

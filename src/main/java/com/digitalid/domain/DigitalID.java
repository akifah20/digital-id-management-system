package com.digitalid.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * A Digital ID has:digitalIdNumber, dateOfBirth, createdDate,
 * firstName,lastName, status, lastModifiedDate
 */
public class DigitalID {

    // Immutable
    private final String digitalIdNumber;
    private final LocalDate dateOfBirth;
    private final LocalDateTime createdDate;

    // Mutable as can be updated by central authority
    private String firstName;
    private String lastName;
    private Status status;
    private LocalDateTime lastModifiedDate;

    //Creates a new Digital ID
    public DigitalID(String firstName, String lastName, LocalDate dateOfBirth) {
        this(generateIdNumber(), firstName, lastName, dateOfBirth, Status.ACTIVE);
    }

    
    // Reconstructs an existing Digital ID when ID number & status are already known
    public DigitalID(String digitalIdNumber, String firstName, String lastName,
            LocalDate dateOfBirth, Status status) {
        this.digitalIdNumber = digitalIdNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.status = status;
        this.createdDate = LocalDateTime.now();
        this.lastModifiedDate = this.createdDate;
    }

    public String getDigitalIdNumber() {
        return digitalIdNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public Status getStatus() {
        return status;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public LocalDateTime getLastModifiedDate() {
        return lastModifiedDate;
    }

    private static String generateIdNumber() {
        return "DID-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}

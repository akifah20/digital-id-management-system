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

    // Creates a new Digital ID
    public DigitalID(String firstName, String lastName, LocalDate dateOfBirth) {
        this(generateIdNumber(), firstName, lastName, dateOfBirth, Status.ACTIVE);
    }

    // Reconstructs an existing Digital ID when ID number & status are already known
    public DigitalID(String digitalIdNumber, String firstName, String lastName,
            LocalDate dateOfBirth, Status status) {
        if (digitalIdNumber == null || digitalIdNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Digital ID number cannot be null or empty!");
        }
        if (firstName == null || firstName.trim().isEmpty()) {
            throw new IllegalArgumentException("First name cannot be null or empty!");
        }
        if (lastName == null || lastName.trim().isEmpty()) {
            throw new IllegalArgumentException("Last name cannot be null or empty!");
        }
        if (dateOfBirth == null) {
            throw new IllegalArgumentException("Date of birth cannot be null!");
        }
        if (dateOfBirth.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Date of birth cannot be in the future!");
        }
        if (status == null) {
            throw new IllegalArgumentException("Status cannot be null!");
        }

        this.digitalIdNumber = digitalIdNumber;
        this.firstName = firstName.trim();
        this.lastName = lastName.trim();
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

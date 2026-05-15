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

    /**
     * updates status of this Digital ID.
     * Validates that the transition is allowed using
     * {@link Status#canChangeTo(Status)}.
     *
     * @param newStatus the new status
     * @throws IllegalArgumentException if newStatus is null
     * @throws IllegalStateException    if the transition is not allowed
     *                                  (e.g. trying to update a REVOKED ID, or
     *                                  moving to the same state)
     */
    public void changeStatus(Status newStatus) {
        if (newStatus == null) {
            throw new IllegalArgumentException("New status cannot be null");
        }
        if (!status.canChangeTo(newStatus)) {
            throw new IllegalStateException(
                    String.format("Cannot change status from %s to %s", status, newStatus));
        }
        this.status = newStatus;
        this.lastModifiedDate = LocalDateTime.now();
    }

    // Updates first name of this ID. Cannot be done if ID is in terminal state
    // (REVOKED/EXPIRED).
    public void updateFirstName(String firstName) {
        checkNotTerminal();
        if (firstName == null || firstName.trim().isEmpty()) {
            throw new IllegalArgumentException("First name cannot be null or empty");
        }
        this.firstName = firstName.trim();
        this.lastModifiedDate = LocalDateTime.now();
    }

    // Updates last name of this ID. Cannot be done if ID is in terminal
    // state(REVOKED/EXPIRED).
    public void updateLastName(String lastName) {
        checkNotTerminal();
        if (lastName == null || lastName.trim().isEmpty()) {
            throw new IllegalArgumentException("Last name cannot be null or empty");
        }
        this.lastName = lastName.trim();
        this.lastModifiedDate = LocalDateTime.now();
    }

    // makes sure terminal IDs cant be changed.
    private void checkNotTerminal() {
        if (status.isTerminal()) {
            throw new IllegalStateException(
                    "Cannot update Digital ID in terminal state: " + status);
        }
    }

    private static String generateIdNumber() {
        return "DID-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}

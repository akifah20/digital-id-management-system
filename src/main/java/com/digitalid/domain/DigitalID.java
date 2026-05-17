package com.digitalid.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class DigitalID {

    private final String digitalIdNumber;
    private final LocalDate dateOfBirth;
    private final LocalDateTime createdDate;

    private String firstName;
    private String lastName;
    private Status status;
    private LocalDateTime lastModifiedDate;

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

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public boolean isValid() {
        return status.isUsable();
    }

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

    public void updateFirstName(String firstName) {
        checkNotTerminal();
        if (firstName == null || firstName.trim().isEmpty()) {
            throw new IllegalArgumentException("First name cannot be null or empty");
        }
        this.firstName = firstName.trim();
        this.lastModifiedDate = LocalDateTime.now();
    }

    public void updateLastName(String lastName) {
        checkNotTerminal();
        if (lastName == null || lastName.trim().isEmpty()) {
            throw new IllegalArgumentException("Last name cannot be null or empty");
        }
        this.lastName = lastName.trim();
        this.lastModifiedDate = LocalDateTime.now();
    }

    private void checkNotTerminal() {
        if (status.isTerminal()) {
            throw new IllegalStateException(
                    "Cannot update Digital ID in terminal state: " + status);
        }
    }

    private static String generateIdNumber() {
        return "DID-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    @Override
    public boolean equals(Object other) {

        if (this == other) {
            return true;
        }

        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        DigitalID that = (DigitalID) other;
        return Objects.equals(this.digitalIdNumber, that.digitalIdNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(digitalIdNumber);
    }

    @Override
    public String toString() {
        return String.format(
                "DigitalID{number='%s', name='%s %s', dob=%s, status=%s}",
                digitalIdNumber, firstName, lastName, dateOfBirth, status);
    }
}

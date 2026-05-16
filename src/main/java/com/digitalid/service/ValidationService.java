package com.digitalid.service;

import java.time.LocalDate;
import java.time.Period;

import com.digitalid.domain.DigitalID;
import com.digitalid.domain.Status;
import com.digitalid.exception.ValidationException;

/**
 * business logic LAYER - apploies validation rules to incoming requests.
 */
public class ValidationService {

    private static final int MIN_NAME_LENGTH = 1;
    private static final int MAX_NAME_LENGTH = 100;
    private static final int MIN_AGE = 0;
    private static final int MAX_AGE = 150;

    public void validateIdentityData(String firstName, String lastName, LocalDate dateOfBirth) { // Validates data
                                                                                                 // needed to create a
                                                                                                 // new ID.
        validateName(firstName, "First name");
        validateName(lastName, "Last name");
        validateDateOfBirth(dateOfBirth);
    }

    public void validateName(String name, String fieldName) {
        if (name == null || name.trim().isEmpty()) {
            throw new ValidationException(fieldName + " is required");
        }
        String trimmed = name.trim();
        if (trimmed.length() < MIN_NAME_LENGTH) {
            throw new ValidationException(
                    fieldName + " must be at least " + MIN_NAME_LENGTH + " character(s)");
        }
        if (trimmed.length() > MAX_NAME_LENGTH) {
            throw new ValidationException(
                    fieldName + " must be no more than " + MAX_NAME_LENGTH + " characters");
        }
    }

    public void validateDateOfBirth(LocalDate dateOfBirth) {
        if (dateOfBirth == null) {
            throw new ValidationException("Date of birth is required");
        }
        if (dateOfBirth.isAfter(LocalDate.now())) {
            throw new ValidationException("Date of birth can't be in the future");
        }
        int age = Period.between(dateOfBirth, LocalDate.now()).getYears();
        if (age < MIN_AGE) {
            throw new ValidationException("Age cannot be negative");
        }
        if (age > MAX_AGE) {
            throw new ValidationException("Age cannot exceed " + MAX_AGE + " years");
        }
    }

    // new status = target status. identity= id being updated
    public void validateStatusTransition(DigitalID identity, Status newStatus) {
        if (identity == null) {
            throw new ValidationException("Identity is required");
        }
        if (newStatus == null) {
            throw new ValidationException("New status is required");
        }
        if (!identity.getStatus().canChangeTo(newStatus)) {
            throw new ValidationException(
                    String.format("Cannot change status from %s to %s",
                            identity.getStatus(), newStatus));
        }
    }

    public void validateUpdateAllowed(DigitalID identity) {
        if (identity == null) {
            throw new ValidationException("Identity is required");
        }
        if (identity.getStatus().isTerminal()) {
            throw new ValidationException(
                    "Cannot update Digital ID in terminal state: " + identity.getStatus());
        }
    }
}

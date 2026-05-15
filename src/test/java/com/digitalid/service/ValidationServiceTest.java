package com.digitalid.service;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.digitalid.domain.DigitalID;
import com.digitalid.domain.Status;
import com.digitalid.exception.ValidationException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ValidationServiceTest {

    private ValidationService service;

    @BeforeEach
    void setUp() {
        service = new ValidationService();
    }

    @Test
    void shouldAcceptValidIdentityData() {
        assertDoesNotThrow(() -> service.validateIdentityData("Hana", "Husssain", LocalDate.of(1990, 1, 15)));
    }

    @Test
    void shouldRejectNullFirstName() {
        assertThrows(ValidationException.class,
                () -> service.validateIdentityData(null, "Husssain", LocalDate.of(1990, 1, 15)));
    }

    @Test
    void shouldRejectEmptyFirstName() {
        assertThrows(ValidationException.class,
                () -> service.validateIdentityData("", "Husssain", LocalDate.of(1990, 1, 15)));
    }

    @Test
    void shouldRejectTooLongFirstName() {
        String longName = "a".repeat(101);
        assertThrows(ValidationException.class,
                () -> service.validateIdentityData(longName, "Husssain", LocalDate.of(1990, 1, 15)));
    }

    @Test
    void shouldRejectFutureDateOfBirth() {
        assertThrows(ValidationException.class,
                () -> service.validateIdentityData("Hana", "Husssain", LocalDate.now().plusDays(1)));
    }

    @Test
    void shouldRejectNullDateOfBirth() {
        assertThrows(ValidationException.class, () -> service.validateIdentityData("Hana", "Husssain", null));
    }

    @Test
    void shouldRejectUnreasonablyOldDateOfBirth() {
        LocalDate tooOld = LocalDate.now().minusYears(151);
        assertThrows(ValidationException.class, () -> service.validateIdentityData("Hana", "Husssain", tooOld));
    }

    @Test
    void shouldAcceptValidStatusTransition() {
        DigitalID id = new DigitalID("Hana", "Husssain", LocalDate.of(1990, 1, 15));

        assertDoesNotThrow(() -> service.validateStatusTransition(id, Status.SUSPENDED));
    }

    @Test
    void shouldRejectNullIdentityForStatusTransition() {
        assertThrows(ValidationException.class, () -> service.validateStatusTransition(null, Status.SUSPENDED));
    }

    @Test
    void shouldRejectNullStatusForTransition() {
        DigitalID id = new DigitalID("Hana", "Husssain", LocalDate.of(1990, 1, 15));

        assertThrows(ValidationException.class, () -> service.validateStatusTransition(id, null));
    }

    @Test
    void shouldRejectInvalidStatusTransition() {
        DigitalID id = new DigitalID("DID-12345678", "Hana", "Husssain",
                LocalDate.of(1990, 1, 15), Status.REVOKED);

        assertThrows(ValidationException.class, () -> service.validateStatusTransition(id, Status.ACTIVE));
    }

    @Test
    void shouldRejectTransitionToSameStatus() {
        DigitalID id = new DigitalID("Hana", "Husssain", LocalDate.of(1990, 1, 15));

        assertThrows(ValidationException.class, () -> service.validateStatusTransition(id, Status.ACTIVE));
    }

    @Test
    void shouldAllowUpdateOnActiveId() {
        DigitalID id = new DigitalID("Hana", "Husssain", LocalDate.of(1990, 1, 15));

        assertDoesNotThrow(() -> service.validateUpdateAllowed(id));
    }

    @Test
    void shouldAllowUpdateOnSuspendedId() {
        DigitalID id = new DigitalID("DID-12345678", "Hana", "Husssain",
                LocalDate.of(1990, 1, 15), Status.SUSPENDED);

        assertDoesNotThrow(() -> service.validateUpdateAllowed(id));
    }

    @Test
    void shouldRejectUpdateOnRevokedId() {
        DigitalID id = new DigitalID("DID-12345678", "Hana", "Husssain",
                LocalDate.of(1990, 1, 15), Status.REVOKED);

        assertThrows(ValidationException.class, () -> service.validateUpdateAllowed(id));
    }

    @Test
    void shouldRejectUpdateOnExpiredId() {
        DigitalID id = new DigitalID("DID-12345678", "Hana", "Husssain",
                LocalDate.of(1990, 1, 15), Status.EXPIRED);

        assertThrows(ValidationException.class, () -> service.validateUpdateAllowed(id));
    }

    @Test
    void shouldRejectUpdateOnNullIdentity() {
        assertThrows(ValidationException.class, () -> service.validateUpdateAllowed(null));
    }
}

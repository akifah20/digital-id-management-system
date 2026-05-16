package com.digitalid.strategy;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.digitalid.domain.DigitalID;
import com.digitalid.domain.Status;
import com.digitalid.domain.VerificationResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ValidityCheckStrategyTest {

    private ValidityCheckStrategy strategy;

    @BeforeEach
    void setUp() {
        strategy = new ValidityCheckStrategy();
    }

    @Test
    void shouldReturnValidForActiveIdentity() {
        DigitalID id = new DigitalID("Hana", "Husssain", LocalDate.of(1990, 1, 15));

        VerificationResponse response = strategy.verify(id);

        assertTrue(response.isValid());
        assertEquals(id.getDigitalIdNumber(), response.getDigitalIdNumber());
        assertEquals(Status.ACTIVE, response.getStatus());
    }

    @Test
    void shouldReturnInvalidForSuspendedIdentity() {
        DigitalID id = new DigitalID("DID-87654321", "Hana", "Husssain",
                LocalDate.of(1990, 1, 15), Status.SUSPENDED);

        VerificationResponse response = strategy.verify(id);

        assertFalse(response.isValid());
        assertEquals(Status.SUSPENDED, response.getStatus());
    }

    @Test
    void shouldReturnInvalidForRevokedIdentity() {
        DigitalID id = new DigitalID("DID-87654321", "Hana", "Husssain",
                LocalDate.of(1990, 1, 15), Status.REVOKED);

        VerificationResponse response = strategy.verify(id);

        assertFalse(response.isValid());
        assertEquals(Status.REVOKED, response.getStatus());
    }

    @Test
    void shouldReturnInvalidForExpiredIdentity() {
        DigitalID id = new DigitalID("DID-87654321", "Hana", "Husssain",
                LocalDate.of(1990, 1, 15), Status.EXPIRED);

        VerificationResponse response = strategy.verify(id);

        assertFalse(response.isValid());
        assertEquals(Status.EXPIRED, response.getStatus());
    }

    @Test
    void shouldNotIncludeAdditionalInfo() {
        DigitalID id = new DigitalID("Hana", "Husssain", LocalDate.of(1990, 1, 15));

        VerificationResponse response = strategy.verify(id);

        assertTrue(response.getAdditionalInfo().isEmpty());
    }

    @Test
    void shouldRejectNullIdentity() {
        assertThrows(IllegalArgumentException.class, () -> strategy.verify(null));
    }
}

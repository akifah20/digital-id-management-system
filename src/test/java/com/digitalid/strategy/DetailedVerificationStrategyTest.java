package com.digitalid.strategy;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.digitalid.domain.DigitalID;
import com.digitalid.domain.Status;
import com.digitalid.domain.VerificationResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DetailedVerificationStrategyTest {

    private DetailedVerificationStrategy strategy;

    @BeforeEach
    void setUp() {
        strategy = new DetailedVerificationStrategy();
    }

    @Test
    void shouldReturnValidForActiveIdentity() {
        DigitalID id = new DigitalID("Hana", "Husssain", LocalDate.of(1990, 1, 15));

        VerificationResponse response = strategy.verify(id);

        assertTrue(response.isValid());
        assertEquals(true, response.getAdditionalInfo().get("currentlyActive"));
        assertEquals(false, response.getAdditionalInfo().get("currentlySuspended"));
    }

    @Test
    void shouldReportSuspendedIdentity() {
        DigitalID id = new DigitalID("DID-87654321", "Hana", "Husssain",
                LocalDate.of(1990, 1, 15), Status.SUSPENDED);

        VerificationResponse response = strategy.verify(id);

        assertFalse(response.isValid());
        assertEquals(false, response.getAdditionalInfo().get("currentlyActive"));
        assertEquals(true, response.getAdditionalInfo().get("currentlySuspended"));
        assertTrue(response.getMessage().toLowerCase().contains("suspend"));
    }

    @Test
    void shouldReportRevokedIdentity() {
        DigitalID id = new DigitalID("DID-87654321", "Hana", "Husssain",
                LocalDate.of(1990, 1, 15), Status.REVOKED);

        VerificationResponse response = strategy.verify(id);

        assertFalse(response.isValid());
        assertEquals(false, response.getAdditionalInfo().get("currentlyActive"));
        assertEquals(false, response.getAdditionalInfo().get("currentlySuspended"));
    }

    @Test
    void shouldIncludeCreatedAndModifiedDates() {
        DigitalID id = new DigitalID("Hana", "Husssain", LocalDate.of(1990, 1, 15));

        VerificationResponse response = strategy.verify(id);

        assertNotNull(response.getAdditionalInfo().get("createdDate"));
        assertNotNull(response.getAdditionalInfo().get("lastModifiedDate"));
    }

    @Test
    void shouldRejectNullIdentity() {
        assertThrows(IllegalArgumentException.class, () -> strategy.verify(null));
    }
}

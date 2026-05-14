package com.digitalid.domain;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for DigitalID entity, where entity = unique identity that we care
 * abt over time
 */
class DigitalIDTest {

    @Test
    void shouldCreateDigitalIdWithGeneratedNumberAndActiveStatus() {
        DigitalID id = new DigitalID("Hana", "Husssain", LocalDate.of(1990, 1, 15));

        assertNotNull(id.getDigitalIdNumber());
        assertEquals("Hana", id.getFirstName());
        assertEquals("Husssain", id.getLastName());
        assertEquals(LocalDate.of(1990, 1, 15), id.getDateOfBirth());
        assertEquals(Status.ACTIVE, id.getStatus());
    }

    @Test
    void generatedIdNumberShouldStartWithDidPrefix() {
        DigitalID id = new DigitalID("Rachel", "Amoyarti", LocalDate.of(1985, 6, 1));

        assertEquals("DID-", id.getDigitalIdNumber().substring(0, 4));
    }

    @Test
    void generatedIdNumbersShouldBeUnique() {
        DigitalID id1 = new DigitalID("Hana", "Husssain", LocalDate.of(1990, 1, 15));
        DigitalID id2 = new DigitalID("Hana", "Husssain", LocalDate.of(1990, 1, 15));

        assertNotEquals(id1.getDigitalIdNumber(), id2.getDigitalIdNumber());
    }

    @Test
    void shouldCreateDigitalIdWithSpecificValues() {
        DigitalID id = new DigitalID(
                "DID-12345678",
                "Rachel",
                "Amoyarti",
                LocalDate.of(1985, 6, 1),
                Status.SUSPENDED);

        assertEquals("DID-12345678", id.getDigitalIdNumber());
        assertEquals("Rachel", id.getFirstName());
        assertEquals("Amoyarti", id.getLastName());
        assertEquals(LocalDate.of(1985, 6, 1), id.getDateOfBirth());
        assertEquals(Status.SUSPENDED, id.getStatus());
    }

    @Test
    void createdDateAndLastModifiedDateShouldBeSetOnCreation() {
        DigitalID id = new DigitalID("Hana", "Husssain", LocalDate.of(1990, 1, 15));

        assertNotNull(id.getCreatedDate());
        assertNotNull(id.getLastModifiedDate());
        assertEquals(id.getCreatedDate(), id.getLastModifiedDate());
    }

    // === Validation tests ===

    @Test
    void shouldRejectNullFirstName() {
        assertThrows(IllegalArgumentException.class,
                () -> new DigitalID(null, "Husssain", LocalDate.of(1990, 1, 15)));
    }

    @Test
    void shouldRejectEmptyFirstName() {
        assertThrows(IllegalArgumentException.class,
                () -> new DigitalID("", "Husssain", LocalDate.of(1990, 1, 15)));
    }

    @Test
    void shouldRejectNullLastName() {
        assertThrows(IllegalArgumentException.class,
                () -> new DigitalID("Hana", null, LocalDate.of(1990, 1, 15)));
    }

    @Test
    void shouldRejectEmptyLastName() {
        assertThrows(IllegalArgumentException.class,
                () -> new DigitalID("Hana", "", LocalDate.of(1990, 1, 15)));
    }

    @Test
    void shouldRejectNullDateOfBirth() {
        assertThrows(IllegalArgumentException.class,
                () -> new DigitalID("Hana", "Husssain", null));
    }

    @Test
    void shouldRejectFutureDateOfBirth() {
        assertThrows(IllegalArgumentException.class,
                () -> new DigitalID("Hana", "Husssain", LocalDate.now().plusDays(1)));
    }

    @Test
    void shouldRejectNullDigitalIdNumber() {
        assertThrows(IllegalArgumentException.class,
                () -> new DigitalID(null, "Hana", "Husssain", LocalDate.of(1990, 1, 15), Status.ACTIVE));
    }

    @Test
    void shouldRejectNullStatus() {
        assertThrows(IllegalArgumentException.class,
                () -> new DigitalID("DID-12345678", "Hana", "Husssain", LocalDate.of(1990, 1, 15), null));
    }

    @Test
    void shouldTrimWhitespaceFromNames() {
        DigitalID id = new DigitalID("  Hana  ", "  Husssain  ", LocalDate.of(1990, 1, 15));

        assertEquals("Hana", id.getFirstName());
        assertEquals("Husssain", id.getLastName());
    }

    // === changeStatus tests ===

    @Test
    void shouldChangeStatusFromActiveToSuspended() {
        DigitalID id = new DigitalID("Hana", "Husssain", LocalDate.of(1990, 1, 15));

        id.changeStatus(Status.SUSPENDED);

        assertEquals(Status.SUSPENDED, id.getStatus());
    }

    @Test
    void shouldChangeStatusFromSuspendedBackToActive() {
        DigitalID id = new DigitalID("DID-12345678", "Hana", "Husssain",
                LocalDate.of(1990, 1, 15), Status.SUSPENDED);

        id.changeStatus(Status.ACTIVE);

        assertEquals(Status.ACTIVE, id.getStatus());
    }

    @Test
    void shouldUpdateLastModifiedDateOnStatusChange() throws InterruptedException {
        DigitalID id = new DigitalID("Hana", "Husssain", LocalDate.of(1990, 1, 15));
        Thread.sleep(10);

        id.changeStatus(Status.SUSPENDED);

        assertTrue(id.getLastModifiedDate().isAfter(id.getCreatedDate()));
    }

    @Test
    void shouldRejectNullStatusChange() {
        DigitalID id = new DigitalID("Hana", "Husssain", LocalDate.of(1990, 1, 15));

        assertThrows(IllegalArgumentException.class, () -> id.changeStatus(null));
    }

    @Test
    void shouldRejectChangingToSameStatus() {
        DigitalID id = new DigitalID("Hana", "Husssain", LocalDate.of(1990, 1, 15));

        assertThrows(IllegalStateException.class, () -> id.changeStatus(Status.ACTIVE));
    }

    @Test
    void shouldRejectChangingFromRevokedState() {
        DigitalID id = new DigitalID("DID-12345678", "Hana", "Husssain",
                LocalDate.of(1990, 1, 15), Status.REVOKED);

        assertThrows(IllegalStateException.class, () -> id.changeStatus(Status.ACTIVE));
    }

    @Test
    void shouldRejectChangingFromExpiredState() {
        DigitalID id = new DigitalID("DID-12345678", "Hana", "Husssain",
                LocalDate.of(1990, 1, 15), Status.EXPIRED);

        assertThrows(IllegalStateException.class, () -> id.changeStatus(Status.ACTIVE));
    }
}

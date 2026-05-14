package com.digitalid.domain;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
    void shouldRejectBlankFirstName() {
        assertThrows(IllegalArgumentException.class,
            () -> new DigitalID("   ", "Husssain", LocalDate.of(1990, 1, 15)));
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
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        assertThrows(IllegalArgumentException.class,
            () -> new DigitalID("Hana", "Husssain", tomorrow));
    }

    @Test
    void shouldRejectNullDigitalIdNumber() {
        assertThrows(IllegalArgumentException.class,
            () -> new DigitalID(null, "Hana", "Husssain", LocalDate.of(1990, 1, 15), Status.ACTIVE));
    }

    @Test
    void shouldRejectEmptyDigitalIdNumber() {
        assertThrows(IllegalArgumentException.class,
            () -> new DigitalID("", "Hana", "Husssain", LocalDate.of(1990, 1, 15), Status.ACTIVE));
    }

    @Test
    void shouldRejectNullStatus() {
        assertThrows(IllegalArgumentException.class,
            () -> new DigitalID("DID-123", "Hana", "Husssain", LocalDate.of(1990, 1, 15), null));
    }

    @Test
    void shouldTrimWhitespaceFromNames() {
        DigitalID id = new DigitalID("  Hana  ", "  Husssain  ", LocalDate.of(1990, 1, 15));

        assertEquals("Hana", id.getFirstName());
        assertEquals("Husssain", id.getLastName());
    }
}

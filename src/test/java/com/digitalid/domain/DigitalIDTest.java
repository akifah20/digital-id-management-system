package com.digitalid.domain;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
}

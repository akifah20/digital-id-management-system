package com.digitalid.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.digitalid.domain.DigitalID;
import com.digitalid.domain.Status;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InMemoryIdentityRepositoryTest {

    private InMemoryIdentityRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryIdentityRepository();
    }

    @Test
    void shouldSaveDigitalId() {
        DigitalID id = new DigitalID("Hana", "Husssain", LocalDate.of(1990, 1, 15));

        repository.save(id);

        assertTrue(repository.exists(id.getDigitalIdNumber()));
    }

    @Test
    void shouldRejectSavingNullIdentity() {
        assertThrows(IllegalArgumentException.class, () -> repository.save(null));
    }

    @Test
    void savingExistingIdShouldReplaceIt() {
        DigitalID original = new DigitalID("DID-12345678", "Hana", "Husssain",
                LocalDate.of(1990, 1, 15), Status.ACTIVE);
        DigitalID updated = new DigitalID("DID-12345678", "Hannah", "Hussain",
                LocalDate.of(1990, 1, 15), Status.SUSPENDED);

        repository.save(original);
        repository.save(updated);

        Optional<DigitalID> found = repository.findById("DID-12345678");
        assertTrue(found.isPresent());
        assertEquals("Hannah", found.get().getFirstName());
        assertEquals(Status.SUSPENDED, found.get().getStatus());
        assertEquals(1, repository.count());
    }

    @Test
    void shouldFindExistingDigitalId() {
        DigitalID id = new DigitalID("Hana", "Husssain", LocalDate.of(1990, 1, 15));
        repository.save(id);

        Optional<DigitalID> found = repository.findById(id.getDigitalIdNumber());

        assertTrue(found.isPresent());
        assertEquals(id, found.get());
    }

    @Test
    void shouldReturnEmptyOptionalForMissingId() {
        Optional<DigitalID> found = repository.findById("DID-NONEXISTENT");

        assertFalse(found.isPresent());
    }

    @Test
    void shouldReturnEmptyOptionalForNullId() {
        Optional<DigitalID> found = repository.findById(null);

        assertFalse(found.isPresent());
    }

    @Test
    void findAllShouldReturnEmptyListWhenNoIds() {
        List<DigitalID> all = repository.findAll();

        assertTrue(all.isEmpty());
    }

    @Test
    void findAllShouldReturnAllSavedIds() {
        DigitalID id1 = new DigitalID("Hana", "Husssain", LocalDate.of(1990, 1, 15));
        DigitalID id2 = new DigitalID("Rachel", "Amoyarti", LocalDate.of(1985, 6, 1));
        repository.save(id1);
        repository.save(id2);

        List<DigitalID> all = repository.findAll();

        assertEquals(2, all.size());
        assertTrue(all.contains(id1));
        assertTrue(all.contains(id2));
    }

    @Test
    void existsShouldReturnFalseForMissingId() {
        assertFalse(repository.exists("DID-NONEXISTENT"));
    }

    @Test
    void existsShouldReturnFalseForNullId() {
        assertFalse(repository.exists(null));
    }

    @Test
    void shouldDeleteExistingId() {
        DigitalID id = new DigitalID("Hana", "Husssain", LocalDate.of(1990, 1, 15));
        repository.save(id);

        repository.delete(id.getDigitalIdNumber());

        assertFalse(repository.exists(id.getDigitalIdNumber()));
    }

    @Test
    void deletingMissingIdShouldNotThrow() {
        repository.delete("DID-NONEXISTENT");
        // No exception, no effect
        assertEquals(0, repository.count());
    }

    @Test
    void deletingNullShouldNotThrow() {
        repository.delete(null);
        assertEquals(0, repository.count());
    }

    @Test
    void countShouldReturnZeroWhenEmpty() {
        assertEquals(0, repository.count());
    }

    @Test
    void countShouldReflectNumberOfSavedIds() {
        repository.save(new DigitalID("Hana", "Husssain", LocalDate.of(1990, 1, 15)));
        repository.save(new DigitalID("Rachel", "Amoyarti", LocalDate.of(1985, 6, 1)));

        assertEquals(2, repository.count());
    }

    @Test
    void clearShouldRemoveAllIds() {
        repository.save(new DigitalID("Hana", "Husssain", LocalDate.of(1990, 1, 15)));
        repository.save(new DigitalID("Rachel", "Amoyarti", LocalDate.of(1985, 6, 1)));

        repository.clear();

        assertEquals(0, repository.count());
        assertTrue(repository.findAll().isEmpty());
    }
}

package com.digitalid.service;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.digitalid.domain.DigitalID;
import com.digitalid.domain.Organisation;
import com.digitalid.domain.OrganisationType;
import com.digitalid.domain.Status;
import com.digitalid.exception.AuthorizationException;
import com.digitalid.exception.ValidationException;
import com.digitalid.repository.InMemoryIdentityRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class IdentityManagementServiceTest {

    private InMemoryIdentityRepository repository;
    private IdentityManagementService service;
    private Organisation centralAuthority;
    private Organisation bank;

    @BeforeEach
    void setUp() {
        repository = new InMemoryIdentityRepository();
        ValidationService validationService = new ValidationService();
        AuthorizationService authorizationService = new AuthorizationService();
        service = new IdentityManagementService(repository, validationService, authorizationService);

        centralAuthority = new Organisation("Home Office", OrganisationType.CENTRAL_AUTHORITY);
        bank = new Organisation("Halifax", OrganisationType.BANK);
    }

    @Test
    void shouldRejectNullRepositoryInConstructor() {
        assertThrows(IllegalArgumentException.class,
                () -> new IdentityManagementService(null, new ValidationService(), new AuthorizationService()));
    }

    @Test
    void shouldRejectNullValidationServiceInConstructor() {
        assertThrows(IllegalArgumentException.class,
                () -> new IdentityManagementService(repository, null, new AuthorizationService()));
    }

    @Test
    void shouldRejectNullAuthorizationServiceInConstructor() {
        assertThrows(IllegalArgumentException.class,
                () -> new IdentityManagementService(repository, new ValidationService(), null));
    }

    @Test
    void shouldCreateIdentityWhenRequestedByCentralAuthority() {
        DigitalID id = service.createIdentity("Hana", "Husssain",
                LocalDate.of(1990, 1, 15), centralAuthority);

        assertNotNull(id);
        assertEquals("Hana", id.getFirstName());
        assertEquals("Husssain", id.getLastName());
        assertEquals(Status.ACTIVE, id.getStatus());
    }

    @Test
    void shouldStoreCreatedIdentityInRepository() {
        DigitalID id = service.createIdentity("Hana", "Husssain",
                LocalDate.of(1990, 1, 15), centralAuthority);

        assertTrue(repository.exists(id.getDigitalIdNumber()));
        assertEquals(1, repository.count());
    }

    @Test
    void shouldRejectCreationByUnauthorisedOrganisation() {
        assertThrows(AuthorizationException.class,
                () -> service.createIdentity("Hana", "Husssain", LocalDate.of(1990, 1, 15), bank));
    }

    @Test
    void shouldNotStoreIdentityWhenAuthorisationFails() {
        try {
            service.createIdentity("Hana", "Husssain", LocalDate.of(1990, 1, 15), bank);
        } catch (AuthorizationException expected) {
            // expected
        }

        assertEquals(0, repository.count());
    }

    @Test
    void shouldRejectCreationWithInvalidFirstName() {
        assertThrows(ValidationException.class,
                () -> service.createIdentity("", "Husssain", LocalDate.of(1990, 1, 15), centralAuthority));
    }

    @Test
    void shouldRejectCreationWithFutureDateOfBirth() {
        assertThrows(ValidationException.class, () -> service.createIdentity("Hana", "Husssain",
                LocalDate.now().plusDays(1), centralAuthority));
    }

    @Test
    void shouldRejectCreationWithNullRequestor() {
        assertThrows(IllegalArgumentException.class,
                () -> service.createIdentity("Hana", "Husssain", LocalDate.of(1990, 1, 15), null));
    }

    @Test
    void shouldGenerateUniqueIdNumbersForEachCreation() {
        DigitalID id1 = service.createIdentity("Hana", "Husssain",
                LocalDate.of(1990, 1, 15), centralAuthority);
        DigitalID id2 = service.createIdentity("Rachel", "Amoyarti",
                LocalDate.of(1985, 6, 1), centralAuthority);

        assertEquals(2, repository.count());
        assertTrue(!id1.getDigitalIdNumber().equals(id2.getDigitalIdNumber()));
    }
}

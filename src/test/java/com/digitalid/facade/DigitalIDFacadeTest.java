package com.digitalid.facade;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.digitalid.domain.DigitalID;
import com.digitalid.domain.Organisation;
import com.digitalid.domain.OrganisationType;
import com.digitalid.domain.Status;
import com.digitalid.domain.VerificationResponse;
import com.digitalid.exception.AuthorizationException;
import com.digitalid.exception.IdentityNotFoundException;
import com.digitalid.exception.ValidationException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Integration tests for DigitalIDFacade - to test full system
 * E2E through the Facade yippee
 */
class DigitalIDFacadeTest {

    private DigitalIDFacade facade;
    private Organisation centralAuthority;
    private Organisation bank;
    private Organisation tax;

    @BeforeEach
    void setUp() {
        facade = new DigitalIDFacade();
        centralAuthority = new Organisation("Home Office", OrganisationType.CENTRAL_AUTHORITY);
        bank = new Organisation("Halifax", OrganisationType.BANK);
        tax = new Organisation("HMRC", OrganisationType.TAX_AUTHORITY);
    }

    @Test
    void shouldRejectNullRepositoryInConstructor() {
        assertThrows(IllegalArgumentException.class, () -> new DigitalIDFacade(null));
    }

    @Test
    void shouldCreateAndVerifyIdentity() {
        DigitalID id = facade.createIdentity("Hana", "Husssain",
                LocalDate.of(1990, 1, 15), centralAuthority);

        VerificationResponse response = facade.verifyIdentity(id.getDigitalIdNumber(), bank);

        assertTrue(response.isValid());
        assertEquals(Status.ACTIVE, response.getStatus());
    }

    @Test
    void shouldCreateAndUpdateIdentity() {
        DigitalID id = facade.createIdentity("Hana", "Husssain",
                LocalDate.of(1990, 1, 15), centralAuthority);

        DigitalID updated = facade.updateName(id.getDigitalIdNumber(),
                "Hannah", "Hussain", centralAuthority);

        assertEquals("Hannah", updated.getFirstName());
        assertEquals("Hussain", updated.getLastName());
    }

    @Test
    void shouldChangeStatusAndAffectVerification() {
        DigitalID id = facade.createIdentity("Hana", "Husssain",
                LocalDate.of(1990, 1, 15), centralAuthority);

        facade.changeStatus(id.getDigitalIdNumber(), Status.SUSPENDED, centralAuthority);
        VerificationResponse response = facade.verifyIdentity(id.getDigitalIdNumber(), bank);

        assertEquals(Status.SUSPENDED, response.getStatus());
        assertTrue(!response.isValid());
    }

    @Test
    void shouldRejectCreationByUnauthorisedOrganisation() {
        assertThrows(AuthorizationException.class,
                () -> facade.createIdentity("Hana", "Husssain", LocalDate.of(1990, 1, 15), bank));
    }

    @Test
    void shouldRejectStatusChangeByUnauthorisedOrganisation() {
        DigitalID id = facade.createIdentity("Hana", "Husssain",
                LocalDate.of(1990, 1, 15), centralAuthority);

        assertThrows(AuthorizationException.class,
                () -> facade.changeStatus(id.getDigitalIdNumber(), Status.SUSPENDED, bank));
    }

    @Test
    void allOrganisationsShouldBeAbleToVerify() {
        DigitalID id = facade.createIdentity("Hana", "Husssain",
                LocalDate.of(1990, 1, 15), centralAuthority);

        assertNotNull(facade.verifyIdentity(id.getDigitalIdNumber(), bank));
        assertNotNull(facade.verifyIdentity(id.getDigitalIdNumber(), tax));
        assertNotNull(facade.verifyIdentity(id.getDigitalIdNumber(), centralAuthority));
    }

    @Test
    void shouldRejectCreationWithInvalidData() {
        assertThrows(ValidationException.class,
                () -> facade.createIdentity("", "Husssain", LocalDate.of(1990, 1, 15), centralAuthority));
    }

    @Test
    void taxAuthorityShouldReceiveDetailedResponse() {
        DigitalID id = facade.createIdentity("Hana", "Husssain",
                LocalDate.of(1990, 1, 15), centralAuthority);

        VerificationResponse response = facade.verifyIdentity(id.getDigitalIdNumber(), tax);

        assertNotNull(response.getAdditionalInfo().get("currentlyActive"));
    }

    @Test
    void bankShouldReceiveSimpleResponse() {
        DigitalID id = facade.createIdentity("Hana", "Husssain",
                LocalDate.of(1990, 1, 15), centralAuthority);

        VerificationResponse response = facade.verifyIdentity(id.getDigitalIdNumber(), bank);

        assertTrue(response.getAdditionalInfo().isEmpty());
    }

    @Test
    void shouldThrowWhenVerifyingMissingIdentity() {
        assertThrows(IdentityNotFoundException.class, () -> facade.verifyIdentity("DID-NONEXISTENT", bank));
    }

    @Test
    void shouldThrowWhenUpdatingMissingIdentity() {
        assertThrows(IdentityNotFoundException.class,
                () -> facade.updateName("DID-NONEXISTENT", "X", "Y", centralAuthority));
    }

    @Test
    void shouldListAllCreatedIdentities() {
        facade.createIdentity("Hana", "Husssain", LocalDate.of(1990, 1, 15), centralAuthority);
        facade.createIdentity("Rachel", "Amoyarti", LocalDate.of(1985, 6, 1), centralAuthority);

        List<DigitalID> all = facade.listAllIdentities();

        assertEquals(2, all.size());
        assertEquals(2, facade.identityCount());
    }

    @Test
    void countShouldBeZeroWhenEmpty() {
        assertEquals(0, facade.identityCount());
        assertTrue(facade.listAllIdentities().isEmpty());
    }
}

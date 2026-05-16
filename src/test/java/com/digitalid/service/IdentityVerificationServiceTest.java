package com.digitalid.service;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.digitalid.domain.DigitalID;
import com.digitalid.domain.Organisation;
import com.digitalid.domain.OrganisationType;
import com.digitalid.domain.Status;
import com.digitalid.domain.VerificationResponse;
import com.digitalid.exception.IdentityNotFoundException;
import com.digitalid.repository.InMemoryIdentityRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class IdentityVerificationServiceTest {

    private InMemoryIdentityRepository repository;
    private IdentityVerificationService service;
    private DigitalID activeIdentity;
    private DigitalID suspendedIdentity;

    private Organisation bank;
    private Organisation tax;
    private Organisation drivingLicence;

    @BeforeEach
    void setUp() {
        repository = new InMemoryIdentityRepository();
        AuthorizationService authorizationService = new AuthorizationService();
        service = new IdentityVerificationService(repository, authorizationService);

        activeIdentity = new DigitalID("Hana", "Husssain", LocalDate.of(1990, 1, 15));
        suspendedIdentity = new DigitalID("DID-87654321", "Rachel", "Amoyarti",
                LocalDate.of(1985, 6, 1), Status.SUSPENDED);

        repository.save(activeIdentity);
        repository.save(suspendedIdentity);

        bank = new Organisation("Halifax", OrganisationType.BANK);
        tax = new Organisation("HMRC", OrganisationType.TAX_AUTHORITY);
        drivingLicence = new Organisation("DVLA", OrganisationType.DRIVING_LICENCE_AUTHORITY);
    }

    @Test
    void shouldRejectNullRepositoryInConstructor() {
        assertThrows(IllegalArgumentException.class,
                () -> new IdentityVerificationService(null, new AuthorizationService()));
    }

    @Test
    void shouldRejectNullAuthorizationServiceInConstructor() {
        assertThrows(IllegalArgumentException.class, () -> new IdentityVerificationService(repository, null));
    }

    @Test
    void bankShouldGetSimpleValidResponseForActiveId() {
        VerificationResponse response = service.verify(activeIdentity.getDigitalIdNumber(), bank);

        assertTrue(response.isValid());
        assertEquals(Status.ACTIVE, response.getStatus());
        assertTrue(response.getAdditionalInfo().isEmpty(),
                "Banks should not see additional info");
    }

    @Test
    void bankShouldGetInvalidResponseForSuspendedId() {
        VerificationResponse response = service.verify(suspendedIdentity.getDigitalIdNumber(), bank);

        assertFalse(response.isValid());
        assertEquals(Status.SUSPENDED, response.getStatus());
    }

    @Test
    void taxAuthorityShouldGetDetailedResponseForActiveId() {
        VerificationResponse response = service.verify(activeIdentity.getDigitalIdNumber(), tax);

        assertTrue(response.isValid());
        assertEquals(true, response.getAdditionalInfo().get("currentlyActive"));
        assertEquals(false, response.getAdditionalInfo().get("currentlySuspended"));
    }

    @Test
    void taxAuthorityShouldGetSuspensionInfoForSuspendedId() {
        VerificationResponse response = service.verify(suspendedIdentity.getDigitalIdNumber(), tax);

        assertFalse(response.isValid());
        assertEquals(true, response.getAdditionalInfo().get("currentlySuspended"));
    }

    @Test
    void drivingLicenceAuthorityShouldGetEligibilityForActiveId() {
        VerificationResponse response = service.verify(activeIdentity.getDigitalIdNumber(), drivingLicence);

        assertTrue(response.isValid());
        assertEquals(true, response.getAdditionalInfo().get("eligibleForLicence"));
        assertEquals(false, response.getAdditionalInfo().get("hasRestriction"));
    }

    @Test
    void drivingLicenceAuthorityShouldDetectRestrictionForSuspendedId() {
        VerificationResponse response = service.verify(suspendedIdentity.getDigitalIdNumber(), drivingLicence);

        assertFalse(response.isValid());
        assertEquals(true, response.getAdditionalInfo().get("hasRestriction"));
        assertEquals(false, response.getAdditionalInfo().get("eligibleForLicence"));
    }

    @Test
    void shouldThrowWhenIdentityNotFound() {
        assertThrows(IdentityNotFoundException.class, () -> service.verify("DID-NONEXISTENT", bank));
    }

    @Test
    void shouldRejectNullRequestor() {
        assertThrows(IllegalArgumentException.class, () -> service.verify(activeIdentity.getDigitalIdNumber(), null));
    }

    @Test
    void centralAuthorityShouldGetSimpleResponse() {
        Organisation centralAuthority = new Organisation("Home Office", OrganisationType.CENTRAL_AUTHORITY);

        VerificationResponse response = service.verify(activeIdentity.getDigitalIdNumber(), centralAuthority);

        // Central auth falls through to the default ValidityCheckStrategy
        assertTrue(response.isValid());
        assertTrue(response.getAdditionalInfo().isEmpty());
    }

    @Test
    void employerShouldGetSimpleResponse() {
        Organisation employer = new Organisation("AstraZeneca", OrganisationType.EMPLOYER);

        VerificationResponse response = service.verify(activeIdentity.getDigitalIdNumber(), employer);

        assertTrue(response.isValid());
        assertTrue(response.getAdditionalInfo().isEmpty());
    }
}

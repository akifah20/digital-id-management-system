package com.digitalid.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.digitalid.domain.Organisation;
import com.digitalid.domain.OrganisationType;
import com.digitalid.exception.AuthorizationException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AuthorizationServiceTest {

    private AuthorizationService service;
    private Organisation centralAuthority;
    private Organisation tax;
    private Organisation bank;

    @BeforeEach
    void setUp() {
        service = new AuthorizationService();
        centralAuthority = new Organisation("Home Office", OrganisationType.CENTRAL_AUTHORITY);
        tax = new Organisation("HMRC", OrganisationType.TAX_AUTHORITY);
        bank = new Organisation("Halifax", OrganisationType.BANK);
    }

    @Test
    void centralAuthorityShouldBeAllowedToCreate() {
        assertDoesNotThrow(() -> service.checkCanCreate(centralAuthority));
    }

    @Test
    void taxAuthorityShouldNotBeAllowedToCreate() {
        assertThrows(AuthorizationException.class, () -> service.checkCanCreate(tax));
    }

    @Test
    void bankShouldNotBeAllowedToCreate() {
        assertThrows(AuthorizationException.class, () -> service.checkCanCreate(bank));
    }

    @Test
    void shouldRejectNullOrganisationOnCreate() {
        assertThrows(IllegalArgumentException.class, () -> service.checkCanCreate(null));
    }

    @Test
    void centralAuthorityShouldBeAllowedToUpdate() {
        assertDoesNotThrow(() -> service.checkCanUpdate(centralAuthority));
    }

    @Test
    void taxAuthorityShouldNotBeAllowedToUpdate() {
        assertThrows(AuthorizationException.class, () -> service.checkCanUpdate(tax));
    }

    @Test
    void bankShouldNotBeAllowedToUpdate() {
        assertThrows(AuthorizationException.class, () -> service.checkCanUpdate(bank));
    }

    @Test
    void centralAuthorityShouldBeAllowedToChangeStatus() {
        assertDoesNotThrow(() -> service.checkCanChangeStatus(centralAuthority));
    }

    @Test
    void taxAuthorityShouldNotBeAllowedToChangeStatus() {
        assertThrows(AuthorizationException.class, () -> service.checkCanChangeStatus(tax));
    }

    @Test
    void employerShouldNotBeAllowedToChangeStatus() {
        Organisation employer = new Organisation("Acme Inc", OrganisationType.EMPLOYER);
        assertThrows(AuthorizationException.class, () -> service.checkCanChangeStatus(employer));
    }

    @Test
    void centralAuthorityShouldBeAllowedToVerify() {
        assertDoesNotThrow(() -> service.checkCanVerify(centralAuthority));
    }

    @Test
    void taxAuthorityShouldBeAllowedToVerify() {
        assertDoesNotThrow(() -> service.checkCanVerify(tax));
    }

    @Test
    void bankShouldBeAllowedToVerify() {
        assertDoesNotThrow(() -> service.checkCanVerify(bank));
    }

    @Test
    void shouldRejectNullOrganisationOnVerify() {
        assertThrows(IllegalArgumentException.class, () -> service.checkCanVerify(null));
    }
}

package com.digitalid.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests OrganisationType
 */
class OrganisationTypeTest {

    @Test
    void shouldHaveSixOrganisationTypes() {
        assertEquals(6, OrganisationType.values().length);
    }

    @Test
    void shouldContainAllExpectedTypes() {
        assertNotNull(OrganisationType.valueOf("CENTRAL_AUTHORITY"));
        assertNotNull(OrganisationType.valueOf("TAX_AUTHORITY"));
        assertNotNull(OrganisationType.valueOf("DRIVING_LICENCE_AUTHORITY"));
        assertNotNull(OrganisationType.valueOf("BANK"));
        assertNotNull(OrganisationType.valueOf("EMPLOYER"));
        assertNotNull(OrganisationType.valueOf("WELFARE_SERVICE"));
    }

    // Display name tests

    @Test
    void shouldHaveCorrectDisplayNames() {
        assertEquals("Central Authority", OrganisationType.CENTRAL_AUTHORITY.getDisplayName());
        assertEquals("Tax Authority", OrganisationType.TAX_AUTHORITY.getDisplayName());
        assertEquals("Driving Licence Authority", OrganisationType.DRIVING_LICENCE_AUTHORITY.getDisplayName());
        assertEquals("Bank", OrganisationType.BANK.getDisplayName());
        assertEquals("Employer", OrganisationType.EMPLOYER.getDisplayName());
        assertEquals("Welfare Service", OrganisationType.WELFARE_SERVICE.getDisplayName());
    }

    @Test
    void displayNameShouldNotBeNull() {
        for (OrganisationType type : OrganisationType.values()) {
            assertNotNull(type.getDisplayName());
        }
    }

    // Permissiontests

    @Test
    void onlyCentralAuthorityShouldManageIdentities() {
        assertTrue(OrganisationType.CENTRAL_AUTHORITY.canManageIdentities());
    }

    @Test
    void otherOrganisationTypesShouldNotManageIdentities() {
        assertFalse(OrganisationType.TAX_AUTHORITY.canManageIdentities());
        assertFalse(OrganisationType.DRIVING_LICENCE_AUTHORITY.canManageIdentities());
        assertFalse(OrganisationType.BANK.canManageIdentities());
        assertFalse(OrganisationType.EMPLOYER.canManageIdentities());
        assertFalse(OrganisationType.WELFARE_SERVICE.canManageIdentities());
    }
}

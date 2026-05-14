package com.digitalid.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for Organisation entity.
 */
class OrganisationTest {

    @Test
    void shouldCreateOrganisationWithGeneratedId() {
        Organisation org = new Organisation("HMRC", OrganisationType.TAX_AUTHORITY);

        assertNotNull(org.getId());
        assertEquals("HMRC", org.getName());
        assertEquals(OrganisationType.TAX_AUTHORITY, org.getType());
    }

    @Test
    void shouldCreateOrganisationWithSpecificId() {
        Organisation org = new Organisation("org-123", "Halifax", OrganisationType.BANK);

        assertEquals("org-123", org.getId());
        assertEquals("Halifax", org.getName());
        assertEquals(OrganisationType.BANK, org.getType());
    }

    @Test
    void generatedIdsShouldBeUnique() {
        Organisation org1 = new Organisation("HMRC", OrganisationType.TAX_AUTHORITY);
        Organisation org2 = new Organisation("HMRC", OrganisationType.TAX_AUTHORITY);

        assertNotEquals(org1.getId(), org2.getId());
    }

    // === Validation tests ===

    @Test
    void shouldRejectNullId() {
        assertThrows(IllegalArgumentException.class,
                () -> new Organisation(null, "HMRC", OrganisationType.TAX_AUTHORITY));
    }

    @Test
    void shouldRejectEmptyId() {
        assertThrows(IllegalArgumentException.class,
                () -> new Organisation("", "HMRC", OrganisationType.TAX_AUTHORITY));
    }

    @Test
    void shouldRejectBlankId() {
        assertThrows(IllegalArgumentException.class,
                () -> new Organisation("   ", "HMRC", OrganisationType.TAX_AUTHORITY));
    }

    @Test
    void shouldRejectNullName() {
        assertThrows(IllegalArgumentException.class,
                () -> new Organisation(null, OrganisationType.TAX_AUTHORITY));
    }

    @Test
    void shouldRejectEmptyName() {
        assertThrows(IllegalArgumentException.class,
                () -> new Organisation("", OrganisationType.TAX_AUTHORITY));
    }

    @Test
    void shouldRejectBlankName() {
        assertThrows(IllegalArgumentException.class,
                () -> new Organisation("   ", OrganisationType.TAX_AUTHORITY));
    }

    @Test
    void shouldRejectNullType() {
        assertThrows(IllegalArgumentException.class,
                () -> new Organisation("HMRC", null));
    }

    @Test
    void shouldTrimWhitespaceFromName() {
        Organisation org = new Organisation("  HMRC  ", OrganisationType.TAX_AUTHORITY);
        assertEquals("HMRC", org.getName());
    }

    // canManageIdentities helper

    @Test
    void centralAuthorityShouldManageIdentities() {
        Organisation org = new Organisation("Home Office", OrganisationType.CENTRAL_AUTHORITY);
        assertTrue(org.canManageIdentities());
    }

    @Test
    void otherOrganisationsShouldNotManageIdentities() {
        Organisation bank = new Organisation("Halifax", OrganisationType.BANK);
        Organisation tax = new Organisation("HMRC", OrganisationType.TAX_AUTHORITY);

        assertFalse(bank.canManageIdentities());
        assertFalse(tax.canManageIdentities());
    }

    // check if orgs + hashcode are equal/ comparison
    @Test
    void organisationsWithSameIdShouldBeEqual() {
        Organisation org1 = new Organisation("org-123", "HMRC", OrganisationType.TAX_AUTHORITY);
        Organisation org2 = new Organisation("org-123", "HMRC", OrganisationType.TAX_AUTHORITY);

        assertEquals(org1, org2);
        assertEquals(org1.hashCode(), org2.hashCode());
    }

    @Test
    void organisationsWithDifferentIdsShouldNotBeEqual() {
        Organisation org1 = new Organisation("org-123", "HMRC", OrganisationType.TAX_AUTHORITY);
        Organisation org2 = new Organisation("org-456", "HMRC", OrganisationType.TAX_AUTHORITY);

        assertNotEquals(org1, org2);
    }

    @Test
    void organisationShouldEqualItself() {
        Organisation org = new Organisation("HMRC", OrganisationType.TAX_AUTHORITY);
        assertEquals(org, org);
    }

    @Test
    void organisationShouldNotEqualNull() {
        Organisation org = new Organisation("HMRC", OrganisationType.TAX_AUTHORITY);
        assertNotEquals(org, null);
    }

    // toString test

    @Test
    void toStringShouldContainKeyFields() {
        Organisation org = new Organisation("org-123", "HMRC", OrganisationType.TAX_AUTHORITY);
        String result = org.toString();

        assertTrue(result.contains("org-123"));
        assertTrue(result.contains("HMRC"));
        assertTrue(result.contains("TAX_AUTHORITY"));
    }
}

package com.digitalid.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
}

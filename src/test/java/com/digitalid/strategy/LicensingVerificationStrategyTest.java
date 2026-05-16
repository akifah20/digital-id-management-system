package com.digitalid.strategy;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.digitalid.domain.DigitalID;
import com.digitalid.domain.Status;
import com.digitalid.domain.VerificationResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LicensingVerificationStrategyTest {

    private LicensingVerificationStrategy strategy;

    @BeforeEach
    void setUp() {
        strategy = new LicensingVerificationStrategy();
    }

    @Test
    void activeIdentityShouldBeEligibleForLicence() {
        DigitalID id = new DigitalID("Hana", "Husssain", LocalDate.of(1990, 1, 15));

        VerificationResponse response = strategy.verify(id);

        assertTrue(response.isValid());
        assertEquals(true, response.getAdditionalInfo().get("eligibleForLicence"));
        assertEquals(false, response.getAdditionalInfo().get("hasRestriction"));
    }

    @Test
    void suspendedIdentityShouldNotBeEligibleAndHaveRestriction() {
        DigitalID id = new DigitalID("DID-87654321", "Hana", "Husssain",
                LocalDate.of(1990, 1, 15), Status.SUSPENDED);

        VerificationResponse response = strategy.verify(id);

        assertFalse(response.isValid());
        assertEquals(false, response.getAdditionalInfo().get("eligibleForLicence"));
        assertEquals(true, response.getAdditionalInfo().get("hasRestriction"));
        assertTrue(response.getMessage().toLowerCase().contains("restriction"));
    }

    @Test
    void revokedIdentityShouldNotBeEligible() {
        DigitalID id = new DigitalID("DID-87654321", "Hana", "Husssain",
                LocalDate.of(1990, 1, 15), Status.REVOKED);

        VerificationResponse response = strategy.verify(id);

        assertFalse(response.isValid());
        assertEquals(false, response.getAdditionalInfo().get("eligibleForLicence"));
        assertEquals(false, response.getAdditionalInfo().get("hasRestriction"));
    }

    @Test
    void expiredIdentityShouldNotBeEligible() {
        DigitalID id = new DigitalID("DID-87654321", "Hana", "Husssain",
                LocalDate.of(1990, 1, 15), Status.EXPIRED);

        VerificationResponse response = strategy.verify(id);

        assertFalse(response.isValid());
        assertEquals(false, response.getAdditionalInfo().get("eligibleForLicence"));
    }

    @Test
    void shouldRejectNullIdentity() {
        assertThrows(IllegalArgumentException.class, () -> strategy.verify(null));
    }
}

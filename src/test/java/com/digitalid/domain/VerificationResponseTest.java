package com.digitalid.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class VerificationResponseTest {

    @Test
    void shouldBuildValidResponse() {
        VerificationResponse response = VerificationResponse.builder()
                .valid(true)
                .digitalIdNumber("DID-87654321")
                .status(Status.ACTIVE)
                .message("All good")
                .build();

        assertTrue(response.isValid());
        assertEquals("DID-87654321", response.getDigitalIdNumber());
        assertEquals(Status.ACTIVE, response.getStatus());
        assertEquals("All good", response.getMessage());
        assertNotNull(response.getVerificationTime());
    }

    @Test
    void shouldUseValidFactoryMethod() {
        VerificationResponse response = VerificationResponse.valid("DID-87654321", Status.ACTIVE);

        assertTrue(response.isValid());
        assertEquals(Status.ACTIVE, response.getStatus());
    }

    @Test
    void shouldUseInvalidFactoryMethod() {
        VerificationResponse response = VerificationResponse.invalid("DID-87654321", "Not found");

        assertFalse(response.isValid());
        assertEquals("Not found", response.getMessage());
    }

    @Test
    void shouldStoreAdditionalInfo() {
        VerificationResponse response = VerificationResponse.builder()
                .valid(true)
                .digitalIdNumber("DID-87654321")
                .addInfo("hasRestrictions", false)
                .addInfo("verifiedBy", "HMRC")
                .build();

        assertEquals(false, response.getAdditionalInfo().get("hasRestrictions"));
        assertEquals("HMRC", response.getAdditionalInfo().get("verifiedBy"));
    }

    @Test
    void additionalInfoMapShouldBeImmutable() {
        VerificationResponse response = VerificationResponse.builder()
                .valid(true)
                .digitalIdNumber("DID-87654321")
                .addInfo("key", "value")
                .build();

        assertThrows(UnsupportedOperationException.class,
                () -> response.getAdditionalInfo().put("hack", "attempt"));
    }
}

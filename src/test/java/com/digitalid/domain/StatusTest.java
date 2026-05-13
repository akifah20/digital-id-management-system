package com.digitalid.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for Status enum.
 */
class StatusTest {

    @Test
    void shouldHaveFourStatusValues() {
        assertEquals(4, Status.values().length);
    }

    @Test
    void shouldContainAllExpectedStatuses() {
        assertNotNull(Status.valueOf("ACTIVE"));
        assertNotNull(Status.valueOf("SUSPENDED"));
        assertNotNull(Status.valueOf("REVOKED"));
        assertNotNull(Status.valueOf("EXPIRED"));
    }

    @Test
    void onlyActiveStatusShouldBeUsable() {
        assertTrue(Status.ACTIVE.isUsable());
        assertFalse(Status.SUSPENDED.isUsable());
        assertFalse(Status.REVOKED.isUsable());
        assertFalse(Status.EXPIRED.isUsable());
    }

    @Test
    void revokedAndExpiredShouldBeTerminal() {
        assertTrue(Status.REVOKED.isTerminal());
        assertTrue(Status.EXPIRED.isTerminal());
    }

    @Test
    void activeAndSuspendedShouldNotBeTerminal() {
        assertFalse(Status.ACTIVE.isTerminal());
        assertFalse(Status.SUSPENDED.isTerminal());
    }
}

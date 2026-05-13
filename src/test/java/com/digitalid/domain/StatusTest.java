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

    // transition tests
    @Test
    void activeShouldTransitionToOtherStates() {
        assertTrue(Status.ACTIVE.canChangeTo(Status.SUSPENDED));
        assertTrue(Status.ACTIVE.canChangeTo(Status.REVOKED));
        assertTrue(Status.ACTIVE.canChangeTo(Status.EXPIRED));
    }

    @Test
    void suspendedShouldTransitionToOtherStates() {
        assertTrue(Status.SUSPENDED.canChangeTo(Status.ACTIVE));
        assertTrue(Status.SUSPENDED.canChangeTo(Status.REVOKED));
        assertTrue(Status.SUSPENDED.canChangeTo(Status.EXPIRED));
    }

    @Test
    void terminalStatesShouldNotTransition() {
        assertFalse(Status.REVOKED.canChangeTo(Status.ACTIVE));
        assertFalse(Status.REVOKED.canChangeTo(Status.SUSPENDED));
        assertFalse(Status.EXPIRED.canChangeTo(Status.ACTIVE));
        assertFalse(Status.EXPIRED.canChangeTo(Status.SUSPENDED));
    }

    @Test
    void shouldNotTransitionToSameState() {
        assertFalse(Status.ACTIVE.canChangeTo(Status.ACTIVE));
        assertFalse(Status.SUSPENDED.canChangeTo(Status.SUSPENDED));
    }

    @Test
    void shouldNotTransitionToNull() {
        assertFalse(Status.ACTIVE.canChangeTo(null));
        assertFalse(Status.SUSPENDED.canChangeTo(null));
    }
}

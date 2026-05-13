package com.digitalid.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

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
}

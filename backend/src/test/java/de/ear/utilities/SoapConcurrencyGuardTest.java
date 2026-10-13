package de.ear.utilities;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SoapConcurrencyGuardTest {

    @Test
    void tryAcquireWorksUntilLimitReached() {
        SoapConcurrencyGuard guard = new SoapConcurrencyGuard();

        for (int i = 0; i < 10; i++) {
            assertTrue(guard.tryAcquire());
        }

        assertFalse(guard.tryAcquire());
    }

    @Test
    void releaseRestoresAvailability() {
        SoapConcurrencyGuard guard = new SoapConcurrencyGuard();

        for (int i = 0; i < 10; i++) {
            guard.tryAcquire();
        }

        guard.release();

        assertTrue(guard.tryAcquire());
    }
}

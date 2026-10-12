package de.ear.utilities;

import org.springframework.stereotype.Component;
import java.util.concurrent.Semaphore;

@Component
public class SoapConcurrencyGuard {

    private final Semaphore semaphore = new Semaphore(10);

    public boolean tryAcquire() {
        return semaphore.tryAcquire();
    }

    public void release() {
        semaphore.release();
    }
}

package de.ear.exceptions;

public class ServiceOverloadedException extends RuntimeException {

    public ServiceOverloadedException() {
        super("Aktuell sind zu viele Anfragen in Bearbeitung. Bitte versuchen Sie es später erneut.");
    }
}

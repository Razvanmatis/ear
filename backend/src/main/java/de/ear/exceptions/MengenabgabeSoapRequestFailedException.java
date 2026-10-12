package de.ear.exceptions;

public class MengenabgabeSoapRequestFailedException extends RuntimeException {

    public MengenabgabeSoapRequestFailedException(String message) {
        super("Fehler bei der Übermittlung an den EAR-SOAP-Service: " + message);
    }
}

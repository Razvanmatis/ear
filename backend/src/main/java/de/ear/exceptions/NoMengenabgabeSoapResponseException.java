package de.ear.exceptions;

public class NoMengenabgabeSoapResponseException extends RuntimeException {

    public NoMengenabgabeSoapResponseException() {
        super("EAR-SOAP-Service hat keine Antwort geliefert.");
    }
}

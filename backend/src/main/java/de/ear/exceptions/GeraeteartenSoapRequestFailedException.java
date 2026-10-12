package de.ear.exceptions;

public class GeraeteartenSoapRequestFailedException extends RuntimeException {

    public GeraeteartenSoapRequestFailedException(String text) {
        super("Gerätearten SOAP Request konnte nicht ausgeführt werden: " + text);
    }
}

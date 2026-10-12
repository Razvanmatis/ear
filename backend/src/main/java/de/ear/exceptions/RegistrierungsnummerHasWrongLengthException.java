package de.ear.exceptions;

public class RegistrierungsnummerHasWrongLengthException extends RuntimeException {

    public RegistrierungsnummerHasWrongLengthException() {
        super("Registrierungsnummer hat die falsche Länge, sie muss 8 Zeichen lang sein!");
    }
}

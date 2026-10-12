package de.ear.exceptions;

import lombok.Getter;

@Getter
public class MengenabgabeSoapRequestNotConfirmedException extends RuntimeException {

    private final int code;
    private final String text;

    public MengenabgabeSoapRequestNotConfirmedException(int code, String text) {
        super("EAR hat die Mengenmitteilung nicht bestätigt. "
                + "Code: " + code
                + ", Text: " + text);
        this.code = code;
        this.text = text;
    }
}

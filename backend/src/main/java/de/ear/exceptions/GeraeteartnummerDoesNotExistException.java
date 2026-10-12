package de.ear.exceptions;

import lombok.Getter;
import java.util.List;

@Getter
public class GeraeteartnummerDoesNotExistException extends RuntimeException {

    private List<Integer> existingGeraeteartnummern;

    public GeraeteartnummerDoesNotExistException(List<Integer> existingGeraeteartnummern) {
        super("Die angegebene Geräteartnummer existiert nicht! Bitte eine gültige wählen aus der Liste: " + existingGeraeteartnummern);
        this.existingGeraeteartnummern = existingGeraeteartnummern;
    }
}

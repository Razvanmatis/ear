package de.ear.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log =
            LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(NoMengenabgabeSoapResponseException.class)
    public ProblemDetail handleNoSoapResponse(
            NoMengenabgabeSoapResponseException ex) {
        return createProblem(
                HttpStatus.BAD_GATEWAY,
                "Keine Antwort vom EAR-Service",
                ex.getMessage()
        );
    }

    @ExceptionHandler(MengenabgabeSoapRequestNotConfirmedException.class)
    public ProblemDetail handleSoapNotConfirmed(
            MengenabgabeSoapRequestNotConfirmedException ex) {
        ProblemDetail problem = createProblem(
                HttpStatus.BAD_GATEWAY,
                "Übermittlung nicht bestätigt",
                ex.getMessage()
        );
        problem.setProperty("soapCode", ex.getCode());
        problem.setProperty("soapMessage", ex.getMessage());
        return problem;
    }

    @ExceptionHandler(MengenabgabeSoapRequestFailedException.class)
    public ProblemDetail handleSoapRequestFailed(
            MengenabgabeSoapRequestFailedException ex) {
        log.error("SOAP-Aufruf an den EAR-Service fehlgeschlagen.", ex);
        return createProblem(
                HttpStatus.BAD_GATEWAY,
                "EAR-Service nicht erreichbar",
                ex.getMessage()
        );
    }

    @ExceptionHandler(RegistrierungsnummerHasWrongLengthException.class)
    public ProblemDetail handleRegistrierungsnummerHasWrongLength(RegistrierungsnummerHasWrongLengthException ex) {
        log.error("Registrierungsnummer hat die falsche Länge, es müssen 8 Zeichen sein!");
        return createProblem(
                HttpStatus.BAD_GATEWAY,
                "Registrierungsnummer hat die falsche Länge",
                ex.getMessage()
        );
    }

    @ExceptionHandler(GeraeteartnummerDoesNotExistException.class)
    public ProblemDetail handleGeraeteartnummerDoesNotExist(GeraeteartnummerDoesNotExistException ex) {
        ProblemDetail problem = createProblem(
                HttpStatus.BAD_GATEWAY,
                "Geräteartnummer existiert nicht!",
                ex.getMessage()
        );
        problem.setProperty("existingGeraeteartnummern", ex.getExistingGeraeteartnummern());
        return problem;
    }

    @ExceptionHandler(GeraeteartenSoapRequestFailedException.class)
    public ProblemDetail handleGeraeteartenSoapRequestFailed(GeraeteartenSoapRequestFailedException ex) {
        log.error("Fehler beim Ermitteln aller Gerätearten", ex);
        return createProblem(
                HttpStatus.BAD_GATEWAY,
                "Fehler beim Ermitteln aller Gerätearten!",
                ex.getMessage()
        );
    }

    @ExceptionHandler(ServiceOverloadedException.class)
    public ProblemDetail handleServiceOverloaded(ServiceOverloadedException ex) {
        log.error("Zuviele Anfragen aktuell.", ex);
        return createProblem(
                HttpStatus.SERVICE_UNAVAILABLE,
                "Zuviele Anfragen aktuell",
                ex.getMessage()
        );
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ProblemDetail handleResponseStatus(ResponseStatusException ex) {
        HttpStatus status = HttpStatus.valueOf(ex.getStatusCode().value());
        return createProblem(status, "Nicht authentifiziert",
                ex.getReason() != null ? ex.getReason() : "Authentifizierung erforderlich");
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleUnexpectedException(Exception ex) {
        log.error("Unerwarteter Backend-Fehler.", ex);
        return createProblem(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Interner Serverfehler",
                "Die Anfrage konnte aufgrund eines internen Fehlers nicht verarbeitet werden."
        );
    }

    private ProblemDetail createProblem(
            HttpStatus status,
            String title,
            String detail) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                status, detail
        );
        problem.setTitle(title);
        return problem;
    }
}

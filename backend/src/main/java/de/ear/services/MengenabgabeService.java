package de.ear.services;

import de.ear.exceptions.*;
import de.ear.mapper.MengenabgabeMapper;
import de.ear.mapper.MengenabgabeRequestMapper;
import de.ear.model.Mengenabgabe;
import de.ear.model.MengenabgabeRequest;
import de.ear.repositories.MengenabgabeRepository;
import de.ear.soap.generated.ServiceException;
import de.ear.soap.generated.StandardResponse;
import de.ear.utilities.SoapConcurrencyGuard;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class MengenabgabeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MengenabgabeService.class);
    private static final String MITTEILUNG_WURDE_ERFOLGREICH_UEBERMITTELT = "Mitteilung wurde erfolgreich übermittelt";
    private static final int LENGTH_REGISTRIERUNGSNUMMER = 8;
    private final MengenabgabeRepository repository;
    private final MengenabgabeMapper mengenabgabeMapper;
    private final MengenabgabeRequestMapper mengenabgabeRequestMapper;
    private final SoapService soapService;
    private final GeraeteartenService geraeteartenService;
    private final SoapConcurrencyGuard soapConcurrencyGuard;

    public MengenabgabeService(MengenabgabeRepository repository, MengenabgabeMapper mengenabgabeMapper,
                               MengenabgabeRequestMapper mengenabgabeRequestMapper, SoapService soapService,
                               GeraeteartenService geraeteartenService, SoapConcurrencyGuard soapConcurrencyGuard) {
        this.repository = repository;
        this.mengenabgabeMapper = mengenabgabeMapper;
        this.mengenabgabeRequestMapper = mengenabgabeRequestMapper;
        this.soapService = soapService;
        this.geraeteartenService = geraeteartenService;
        this.soapConcurrencyGuard = soapConcurrencyGuard;
    }

    @Transactional
    public Mengenabgabe createMengenabgabe(MengenabgabeRequest body) {
        validateRequest(body);
        sendMengenabgabeSoapRequest(body);
        return mengenabgabeMapper.toModel(repository.save(mengenabgabeRequestMapper.toEntity(body)));
    }

    private void validateRequest(MengenabgabeRequest body) {
        if (body.getRegistrierungsnummer().toString().length() != LENGTH_REGISTRIERUNGSNUMMER) {
            LOGGER.error("Registrierungsnummer hat falsche Länge.");
            throw new RegistrierungsnummerHasWrongLengthException();
        }
        if (geraeteartenService.geraeteartnummerDoesNotExist(body.getGeraeteartnummer())) {
            LOGGER.error("Geraeteartnummer {} existiert nicht.", body.getGeraeteartnummer());
            throw new GeraeteartnummerDoesNotExistException(geraeteartenService.getExistingGerateartnummern());
        }
    }

    private void sendMengenabgabeSoapRequest(MengenabgabeRequest body) {
        if (!soapConcurrencyGuard.tryAcquire()) {
            LOGGER.error("SOAP Service ist überlastet. Bitte versuchen Sie es später erneut.");
            throw new ServiceOverloadedException();
        }
        try {
            StandardResponse response = soapService.submitIstInput(
                    body.getMenge(),
                    body.getEinheit(),
                    body.getJahr(),
                    body.getMonat(),
                    body.getGeraeteartnummer(),
                    body.getRegistrierungsnummer(),
                    body.getWahrheitserklaerung()
            );
            if (response == null) {
                LOGGER.error("SOAP Service hat keine Antwort zurückgegeben.");
                throw new NoMengenabgabeSoapResponseException();
            }
            if (!MITTEILUNG_WURDE_ERFOLGREICH_UEBERMITTELT.equals(response.getText())) {
                LOGGER.error("Mengenabgabe-Soap-Anfrage wurde nicht bestätigt. Code: {}, Text: {}", response.getCode(), response.getText());
                throw new MengenabgabeSoapRequestNotConfirmedException(response.getCode(), response.getText());
            }
        } catch (ServiceException e) {
            LOGGER.error("Fehler beim Senden der Mengenabgabe-Soap-Anfrage: {}", e.getMessage());
            throw new MengenabgabeSoapRequestFailedException(e.getMessage());
        } finally {
            soapConcurrencyGuard.release();
        }
    }

    @Transactional
    public Page<Mengenabgabe> getMengenabgaben(Pageable pageable) {
        return repository.findAll(pageable)
                .map(mengenabgabeMapper::toModel);
    }
}

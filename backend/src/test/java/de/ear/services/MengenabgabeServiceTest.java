package de.ear.services;

import de.ear.entities.MengenabgabeEntity;
import de.ear.exceptions.GeraeteartnummerDoesNotExistException;
import de.ear.exceptions.RegistrierungsnummerHasWrongLengthException;
import de.ear.mapper.MengenabgabeMapper;
import de.ear.mapper.MengenabgabeRequestMapper;
import de.ear.model.Mengenabgabe;
import de.ear.model.MengenabgabeRequest;
import de.ear.repositories.MengenabgabeRepository;
import de.ear.soap.generated.StandardResponse;
import de.ear.utilities.SoapConcurrencyGuard;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class MengenabgabeServiceTest {

    @Test
    void createMengenabgabeSavesAndReturnsMappedEntity() throws de.ear.soap.generated.ServiceException {
        MengenabgabeRepository repository = mock(MengenabgabeRepository.class);
        MengenabgabeMapper mengenabgabeMapper = mock(MengenabgabeMapper.class);
        MengenabgabeRequestMapper mengenabgabeRequestMapper = mock(MengenabgabeRequestMapper.class);
        SoapService soapService = mock(SoapService.class);
        GeraeteartenService geraeteartenService = mock(GeraeteartenService.class);
        SoapConcurrencyGuard soapConcurrencyGuard = mock(SoapConcurrencyGuard.class);
        MengenabgabeService service = new MengenabgabeService(
                repository,
                mengenabgabeMapper,
                mengenabgabeRequestMapper,
                soapService,
                geraeteartenService,
                soapConcurrencyGuard
        );

        MengenabgabeRequest body = new MengenabgabeRequest();
        body.setMenge(new BigDecimal("15.75"));
        body.setEinheit(1);
        body.setJahr(2025);
        body.setMonat(7);
        body.setGeraeteartnummer(12);
        body.setRegistrierungsnummer(12345678);
        body.setWahrheitserklaerung(true);

        MengenabgabeEntity entity = new MengenabgabeEntity(
                new BigDecimal("15.75"),
                1,
                2025,
                7,
                12,
                12345678,
                true
        );
        entity.setId(55L);

        Mengenabgabe response = new Mengenabgabe();
        response.setId(55L);
        response.setMenge(new BigDecimal("15.75"));
        response.setEinheit(1);
        response.setJahr(2025);
        response.setMonat(7);
        response.setGeraeteartnummer(12);
        response.setRegistrierungsnummer(12345678);
        response.setWahrheitserklaerung(true);

        when(geraeteartenService.geraeteartnummerDoesNotExist(12)).thenReturn(false);
        when(soapConcurrencyGuard.tryAcquire()).thenReturn(true);
        when(soapService.submitIstInput(
                new BigDecimal("15.75"),
                1,
                2025,
                7,
                12,
                12345678,
                true
        )).thenReturn(responseFromSoap("Mitteilung wurde erfolgreich übermittelt"));
        when(mengenabgabeRequestMapper.toEntity(body)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(entity);
        when(mengenabgabeMapper.toModel(entity)).thenReturn(response);

        Mengenabgabe result = service.createMengenabgabe(body);

        assertSame(response, result);
        verify(soapConcurrencyGuard).release();
    }

    @Test
    void createMengenabgabeRejectsWrongRegistrationLength() {
        MengenabgabeRepository repository = mock(MengenabgabeRepository.class);
        MengenabgabeMapper mengenabgabeMapper = mock(MengenabgabeMapper.class);
        MengenabgabeRequestMapper mengenabgabeRequestMapper = mock(MengenabgabeRequestMapper.class);
        SoapService soapService = mock(SoapService.class);
        GeraeteartenService geraeteartenService = mock(GeraeteartenService.class);
        SoapConcurrencyGuard soapConcurrencyGuard = mock(SoapConcurrencyGuard.class);
        MengenabgabeService service = new MengenabgabeService(
                repository,
                mengenabgabeMapper,
                mengenabgabeRequestMapper,
                soapService,
                geraeteartenService,
                soapConcurrencyGuard
        );

        MengenabgabeRequest body = new MengenabgabeRequest();
        body.setMenge(new BigDecimal("5"));
        body.setEinheit(1);
        body.setJahr(2025);
        body.setMonat(1);
        body.setGeraeteartnummer(12);
        body.setRegistrierungsnummer(1234567);
        body.setWahrheitserklaerung(true);

        assertThrows(RegistrierungsnummerHasWrongLengthException.class, () -> service.createMengenabgabe(body));
    }

    @Test
    void createMengenabgabeRejectsUnknownGeraeteartnummer() {
        MengenabgabeRepository repository = mock(MengenabgabeRepository.class);
        MengenabgabeMapper mengenabgabeMapper = mock(MengenabgabeMapper.class);
        MengenabgabeRequestMapper mengenabgabeRequestMapper = mock(MengenabgabeRequestMapper.class);
        SoapService soapService = mock(SoapService.class);
        GeraeteartenService geraeteartenService = mock(GeraeteartenService.class);
        SoapConcurrencyGuard soapConcurrencyGuard = mock(SoapConcurrencyGuard.class);
        MengenabgabeService service = new MengenabgabeService(
                repository,
                mengenabgabeMapper,
                mengenabgabeRequestMapper,
                soapService,
                geraeteartenService,
                soapConcurrencyGuard
        );

        MengenabgabeRequest body = new MengenabgabeRequest();
        body.setMenge(new BigDecimal("5"));
        body.setEinheit(1);
        body.setJahr(2025);
        body.setMonat(1);
        body.setGeraeteartnummer(12);
        body.setRegistrierungsnummer(12345678);
        body.setWahrheitserklaerung(true);

        when(geraeteartenService.geraeteartnummerDoesNotExist(12)).thenReturn(true);
        when(geraeteartenService.getExistingGerateartnummern()).thenReturn(List.of(1, 2, 3));

        assertThrows(GeraeteartnummerDoesNotExistException.class, () -> service.createMengenabgabe(body));
    }

    @Test
    void getMengenabgabenMapsRepositoryPage() {
        MengenabgabeRepository repository = mock(MengenabgabeRepository.class);
        MengenabgabeMapper mengenabgabeMapper = mock(MengenabgabeMapper.class);
        MengenabgabeRequestMapper mengenabgabeRequestMapper = mock(MengenabgabeRequestMapper.class);
        SoapService soapService = mock(SoapService.class);
        GeraeteartenService geraeteartenService = mock(GeraeteartenService.class);
        SoapConcurrencyGuard soapConcurrencyGuard = mock(SoapConcurrencyGuard.class);
        MengenabgabeService service = new MengenabgabeService(
                repository,
                mengenabgabeMapper,
                mengenabgabeRequestMapper,
                soapService,
                geraeteartenService,
                soapConcurrencyGuard
        );

        Pageable pageable = PageRequest.of(1, 10);
        MengenabgabeEntity entity = new MengenabgabeEntity(
                new BigDecimal("1.2"),
                2,
                2024,
                8,
                4,
                87654321,
                false
        );
        entity.setId(7L);

        Mengenabgabe model = new Mengenabgabe();
        model.setId(7L);
        model.setMenge(new BigDecimal("1.2"));
        model.setEinheit(2);
        model.setJahr(2024);
        model.setMonat(8);
        model.setGeraeteartnummer(4);
        model.setRegistrierungsnummer(87654321);
        model.setWahrheitserklaerung(false);

        when(repository.findAll(pageable)).thenReturn(new PageImpl<>(List.of(entity), pageable, 1));
        when(mengenabgabeMapper.toModel(entity)).thenReturn(model);

        Page<Mengenabgabe> result = service.getMengenabgaben(pageable);

        assertEquals(1, result.getContent().size());
        assertEquals(model, result.getContent().get(0));
    }

    private StandardResponse responseFromSoap(String text) {
        StandardResponse response = new StandardResponse();
        response.setCode(0);
        response.setText(text);
        return response;
    }
}

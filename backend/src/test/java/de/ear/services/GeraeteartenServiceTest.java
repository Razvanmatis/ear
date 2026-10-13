package de.ear.services;

import de.ear.exceptions.GeraeteartenSoapRequestFailedException;
import de.ear.mapper.GeraeteartMapper;
import de.ear.soap.generated.ServiceException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GeraeteartenServiceTest {

    @Test
    void getGeraeteartenMapsSoapEntriesToModels() throws ServiceException {
        SoapService soapService = mock(SoapService.class);
        GeraeteartMapper mapper = mock(GeraeteartMapper.class);
        GeraeteartenService service = new GeraeteartenService(soapService, mapper);

        de.ear.soap.generated.Geraeteart soapFirst = new de.ear.soap.generated.Geraeteart();
        de.ear.model.Geraeteart modelFirst = new de.ear.model.Geraeteart();
        modelFirst.setNummer(1);

        de.ear.soap.generated.Geraeteart soapSecond = new de.ear.soap.generated.Geraeteart();
        de.ear.model.Geraeteart modelSecond = new de.ear.model.Geraeteart();
        modelSecond.setNummer(2);

        when(soapService.getGeraetearten()).thenReturn(List.of(soapFirst, soapSecond));
        when(mapper.toModel(soapFirst)).thenReturn(modelFirst);
        when(mapper.toModel(soapSecond)).thenReturn(modelSecond);

        de.ear.model.Geraeteart[] result = service.getGeraetearten();

        assertArrayEquals(new Integer[]{1, 2}, new Integer[]{result[0].getNummer(), result[1].getNummer()});
    }

    @Test
    void getGeraeteartenThrowsApplicationExceptionWhenSoapCallFails() throws ServiceException {
        SoapService soapService = mock(SoapService.class);
        GeraeteartMapper mapper = mock(GeraeteartMapper.class);
        GeraeteartenService service = new GeraeteartenService(soapService, mapper);

        when(soapService.getGeraetearten()).thenThrow(new ServiceException("soap down", null));

        assertThrows(GeraeteartenSoapRequestFailedException.class, service::getGeraetearten);
    }

    @Test
    void geraeteartnummerDoesNotExistReturnsTrueWhenNumberIsMissing() throws ServiceException {
        SoapService soapService = mock(SoapService.class);
        GeraeteartMapper mapper = mock(GeraeteartMapper.class);
        GeraeteartenService service = new GeraeteartenService(soapService, mapper);

        de.ear.soap.generated.Geraeteart soap = new de.ear.soap.generated.Geraeteart();
        de.ear.model.Geraeteart model = new de.ear.model.Geraeteart();
        model.setNummer(7);

        when(soapService.getGeraetearten()).thenReturn(List.of(soap));
        when(mapper.toModel(soap)).thenReturn(model);

        assertFalse(service.geraeteartnummerDoesNotExist(7));
        assertEquals(List.of(7), service.getExistingGerateartnummern());
    }
}

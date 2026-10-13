package de.ear.mapper;

import de.ear.entities.MengenabgabeEntity;
import de.ear.model.MengenabgabeRequest;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class MengenabgabeRequestMapperTest {

    private final MengenabgabeRequestMapper mapper = new MengenabgabeRequestMapperImpl();

    @Test
    void mapsRequestToEntity() {
        MengenabgabeRequest request = new MengenabgabeRequest();
        request.setMenge(new BigDecimal("8.25"));
        request.setEinheit(4);
        request.setJahr(2026);
        request.setMonat(11);
        request.setGeraeteartnummer(9);
        request.setRegistrierungsnummer(4321);
        request.setWahrheitserklaerung(false);

        MengenabgabeEntity entity = mapper.toEntity(request);

        assertAll(
                () -> assertNull(entity.getId()),
                () -> assertEquals(request.getMenge(), entity.getMenge()),
                () -> assertEquals(request.getEinheit(), entity.getEinheit()),
                () -> assertEquals(request.getJahr(), entity.getJahr()),
                () -> assertEquals(request.getMonat(), entity.getMonat()),
                () -> assertEquals(request.getGeraeteartnummer(), entity.getGeraeteartnummer()),
                () -> assertEquals(request.getRegistrierungsnummer(), entity.getRegistrierungsnummer()),
                () -> assertEquals(request.getWahrheitserklaerung(), entity.getWahrheitserklaerung())
        );
    }

    @Test
    void returnsNullWhenRequestIsNull() {
        assertNull(mapper.toEntity(null));
    }
}

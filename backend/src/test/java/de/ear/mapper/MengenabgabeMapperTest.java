package de.ear.mapper;

import de.ear.entities.MengenabgabeEntity;
import de.ear.model.Mengenabgabe;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class MengenabgabeMapperTest {

    private final MengenabgabeMapper mapper = new MengenabgabeMapperImpl();

    @Test
    void mapsEntityToModel() {
        MengenabgabeEntity entity = new MengenabgabeEntity(
                new BigDecimal("12.50"),
                2,
                2025,
                3,
                7,
                99,
                true
        );
        entity.setId(123L);

        Mengenabgabe model = mapper.toModel(entity);

        assertAll(
                () -> assertEquals(entity.getId(), model.getId()),
                () -> assertEquals(entity.getMenge(), model.getMenge()),
                () -> assertEquals(entity.getEinheit(), model.getEinheit()),
                () -> assertEquals(entity.getJahr(), model.getJahr()),
                () -> assertEquals(entity.getMonat(), model.getMonat()),
                () -> assertEquals(entity.getGeraeteartnummer(), model.getGeraeteartnummer()),
                () -> assertEquals(entity.getRegistrierungsnummer(), model.getRegistrierungsnummer()),
                () -> assertEquals(entity.getWahrheitserklaerung(), model.getWahrheitserklaerung())
        );
    }

    @Test
    void returnsNullWhenEntityIsNull() {
        assertNull(mapper.toModel(null));
    }
}

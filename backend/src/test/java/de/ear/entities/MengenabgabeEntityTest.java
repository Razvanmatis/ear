package de.ear.entities;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class MengenabgabeEntityTest {

    @Test
    void defaultConstructorCreatesEmptyEntity() {
        MengenabgabeEntity entity = new MengenabgabeEntity();

        assertAll(
                () -> assertNull(entity.getId()),
                () -> assertNull(entity.getMenge()),
                () -> assertNull(entity.getEinheit()),
                () -> assertNull(entity.getJahr()),
                () -> assertNull(entity.getMonat()),
                () -> assertNull(entity.getGeraeteartnummer()),
                () -> assertNull(entity.getRegistrierungsnummer()),
                () -> assertNull(entity.getWahrheitserklaerung())
        );
    }

    @Test
    void constructorSetsAllFields() {
        BigDecimal menge = new BigDecimal("42.75");

        MengenabgabeEntity entity = new MengenabgabeEntity(
                menge,
                3,
                2025,
                10,
                17,
                4711,
                true
        );

        assertAll(
                () -> assertEquals(menge, entity.getMenge()),
                () -> assertEquals(3, entity.getEinheit()),
                () -> assertEquals(2025, entity.getJahr()),
                () -> assertEquals(10, entity.getMonat()),
                () -> assertEquals(17, entity.getGeraeteartnummer()),
                () -> assertEquals(4711, entity.getRegistrierungsnummer()),
                () -> assertEquals(true, entity.getWahrheitserklaerung())
        );
    }

    @Test
    void settersUpdateValues() {
        MengenabgabeEntity entity = new MengenabgabeEntity();

        entity.setId(99L);
        entity.setMenge(new BigDecimal("12.50"));
        entity.setEinheit(4);
        entity.setJahr(2024);
        entity.setMonat(6);
        entity.setGeraeteartnummer(8);
        entity.setRegistrierungsnummer(1234);
        entity.setWahrheitserklaerung(false);

        assertAll(
                () -> assertEquals(99L, entity.getId()),
                () -> assertEquals(new BigDecimal("12.50"), entity.getMenge()),
                () -> assertEquals(4, entity.getEinheit()),
                () -> assertEquals(2024, entity.getJahr()),
                () -> assertEquals(6, entity.getMonat()),
                () -> assertEquals(8, entity.getGeraeteartnummer()),
                () -> assertEquals(1234, entity.getRegistrierungsnummer()),
                () -> assertEquals(false, entity.getWahrheitserklaerung())
        );
    }
}

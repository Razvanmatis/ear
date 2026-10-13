package de.ear.mapper;

import de.ear.model.Geraeteart;
import de.ear.soap.generated.Gruppe;
import de.ear.soap.generated.Kategorie;
import jakarta.xml.bind.JAXBElement;
import org.junit.jupiter.api.Test;

import javax.xml.namespace.QName;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class GeraeteartMapperTest {

    private final GeraeteartMapper mapper = new GeraeteartMapperImpl();

    @Test
    void mapsSoapObjectToModel() {
        de.ear.soap.generated.Geraeteart soap = new de.ear.soap.generated.Geraeteart();
        soap.setNummer(15);
        soap.setName("Wasserkocher");

        Gruppe gruppe = new Gruppe();
        gruppe.setNummer(3);
        gruppe.setName("Haushalt");
        JAXBElement<Gruppe> gruppeElement = new JAXBElement<>(new QName("gruppe"), Gruppe.class, gruppe);
        soap.setGruppe(gruppeElement);

        Kategorie kategorie = new Kategorie();
        kategorie.setNummer(2);
        kategorie.setName("Elektro");
        soap.setKategorie(kategorie);

        Geraeteart model = mapper.toModel(soap);

        assertAll(
                () -> assertEquals(15, model.getNummer()),
                () -> assertEquals("Wasserkocher", model.getName()),
                () -> assertEquals("Haushalt", model.getGruppe()),
                () -> assertEquals("Elektro", model.getKategorie())
        );
    }

    @Test
    void handlesMissingGroup() {
        de.ear.soap.generated.Geraeteart soap = new de.ear.soap.generated.Geraeteart();
        soap.setNummer(7);
        soap.setName("Licht");

        Kategorie kategorie = new Kategorie();
        kategorie.setName("Beleuchtung");
        soap.setKategorie(kategorie);

        Geraeteart model = mapper.toModel(soap);

        assertAll(
                () -> assertEquals(7, model.getNummer()),
                () -> assertEquals("Licht", model.getName()),
                () -> assertNull(model.getGruppe()),
                () -> assertEquals("Beleuchtung", model.getKategorie())
        );
    }

    @Test
    void returnsNullWhenSoapObjectIsNull() {
        assertNull(mapper.toModel(null));
    }
}

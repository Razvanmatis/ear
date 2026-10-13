package de.ear.utilities;

import de.ear.model.Mengenabgabe;
import de.ear.model.MengenabgabePage;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MengenabgabeControllerUtilitiesTest {

    @Test
    void createSortReturnsUnsortedWhenParametersMissing() {
        Sort sort = MengenabgabeControllerUtilities.createSort(null);

        assertEquals(Sort.unsorted(), sort);
    }

    @Test
    void createSortAcceptsValidParameters() {
        Sort sort = MengenabgabeControllerUtilities.createSort(new String[]{"jahr,desc", "monat"});

        assertAll(
                () -> assertEquals(Sort.Direction.DESC, sort.getOrderFor("jahr").getDirection()),
                () -> assertEquals(Sort.Direction.ASC, sort.getOrderFor("monat").getDirection())
        );
    }

    @Test
    void createSortAcceptsParametersSplitBySpringStringArrayBinding() {
        Sort sort = MengenabgabeControllerUtilities.createSort(
                new String[]{"menge", "desc", "jahr", "asc"});

        assertAll(
                () -> assertEquals(Sort.Direction.DESC, sort.getOrderFor("menge").getDirection()),
                () -> assertEquals(Sort.Direction.ASC, sort.getOrderFor("jahr").getDirection())
        );
    }

    @Test
    void createSortRejectsInvalidFields() {
        assertThrows(IllegalArgumentException.class,
                () -> MengenabgabeControllerUtilities.createSort(new String[]{"name,asc"}));
    }

    @Test
    void createSortRejectsInvalidFormat() {
        assertThrows(IllegalArgumentException.class,
                () -> MengenabgabeControllerUtilities.createSort(new String[]{"jahr,desc,extra"}));
    }

    @Test
    void getPageableUsesDefaultsWhenValuesMissing() {
        Pageable pageable = MengenabgabeControllerUtilities.getPageable(null, null, null);

        assertAll(
                () -> assertEquals(0, pageable.getPageNumber()),
                () -> assertEquals(20, pageable.getPageSize()),
                () -> assertTrue(pageable.getSort().isUnsorted())
        );
    }

    @Test
    void getPageableUsesProvidedValuesAndSort() {
        Pageable pageable = MengenabgabeControllerUtilities.getPageable(2, 5, new String[]{"jahr,desc"});

        assertAll(
                () -> assertEquals(2, pageable.getPageNumber()),
                () -> assertEquals(5, pageable.getPageSize()),
                () -> assertEquals(Sort.Direction.DESC, pageable.getSort().getOrderFor("jahr").getDirection())
        );
    }

    @Test
    void createResponseMapsPageToPageModel() {
        Mengenabgabe first = new Mengenabgabe();
        first.setId(1L);
        first.setMenge(new BigDecimal("10.5"));
        first.setEinheit(1);
        first.setJahr(2024);
        first.setMonat(3);
        first.setGeraeteartnummer(7);
        first.setRegistrierungsnummer(1001);
        first.setWahrheitserklaerung(true);

        Mengenabgabe second = new Mengenabgabe();
        second.setId(2L);
        second.setMenge(new BigDecimal("20.0"));
        second.setEinheit(2);
        second.setJahr(2025);
        second.setMonat(4);
        second.setGeraeteartnummer(8);
        second.setRegistrierungsnummer(1002);
        second.setWahrheitserklaerung(false);

        Page<Mengenabgabe> page = new PageImpl<>(List.of(first, second), org.springframework.data.domain.PageRequest.of(1, 2), 5);

        MengenabgabePage response = MengenabgabeControllerUtilities.createResponse(page);

        assertAll(
                () -> assertEquals(2, response.getContent().length),
                () -> assertEquals(1, response.getPage()),
                () -> assertEquals(2, response.getSize()),
                () -> assertEquals(5, response.getTotalElements()),
                () -> assertEquals(3, response.getTotalPages()),
                () -> assertEquals(1L, response.getContent()[0].getId()),
                () -> assertEquals(2L, response.getContent()[1].getId())
        );
    }
}

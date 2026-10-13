package de.ear.controller;

import de.ear.model.Mengenabgabe;
import de.ear.model.MengenabgabeRequest;
import de.ear.services.MengenabgabeService;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MengenabgabeControllerIntegrationTest {

    private final MengenabgabeService service = mock(MengenabgabeService.class);
    private final MockMvc mockMvc = MockMvcBuilders.standaloneSetup(new MengenabgabeController(service)).build();

    @Test
    void createMengenabgabeReturnsCreatedResponse() throws Exception {
        MengenabgabeRequest request = new MengenabgabeRequest();
        request.setMenge(new BigDecimal("12.50"));
        request.setEinheit(1);
        request.setJahr(2025);
        request.setMonat(6);
        request.setGeraeteartnummer(7);
        request.setRegistrierungsnummer(12345678);
        request.setWahrheitserklaerung(true);

        Mengenabgabe response = new Mengenabgabe();
        response.setId(99L);
        response.setMenge(new BigDecimal("12.50"));
        response.setEinheit(1);
        response.setJahr(2025);
        response.setMonat(6);
        response.setGeraeteartnummer(7);
        response.setRegistrierungsnummer(12345678);
        response.setWahrheitserklaerung(true);

        when(service.createMengenabgabe(any(MengenabgabeRequest.class))).thenReturn(response);

        mockMvc.perform(post("/mengenabgaben")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"menge\":12.50,\"einheit\":1,\"jahr\":2025,\"monat\":6,\"geraeteartnummer\":7,\"registrierungsnummer\":12345678,\"wahrheitserklaerung\":true}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(99))
                .andExpect(jsonPath("$.menge").value(12.5))
                .andExpect(jsonPath("$.einheit").value(1))
                .andExpect(jsonPath("$.jahr").value(2025))
                .andExpect(jsonPath("$.monat").value(6))
                .andExpect(jsonPath("$.geraeteartnummer").value(7))
                .andExpect(jsonPath("$.registrierungsnummer").value(12345678))
                .andExpect(jsonPath("$.wahrheitserklaerung").value(true));
    }

    @Test
    void getMengenabgabenReturnsPagedResponse() throws Exception {
        Mengenabgabe first = new Mengenabgabe();
        first.setId(1L);
        first.setMenge(new BigDecimal("10.0"));
        first.setEinheit(1);
        first.setJahr(2024);
        first.setMonat(2);
        first.setGeraeteartnummer(3);
        first.setRegistrierungsnummer(12345678);
        first.setWahrheitserklaerung(true);

        Mengenabgabe second = new Mengenabgabe();
        second.setId(2L);
        second.setMenge(new BigDecimal("20.5"));
        second.setEinheit(2);
        second.setJahr(2025);
        second.setMonat(3);
        second.setGeraeteartnummer(4);
        second.setRegistrierungsnummer(87654321);
        second.setWahrheitserklaerung(false);

        Page<Mengenabgabe> page = new PageImpl<>(List.of(first, second), org.springframework.data.domain.PageRequest.of(1, 2), 5);
        when(service.getMengenabgaben(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/mengenabgaben")
                        .param("page", "1")
                        .param("size", "2")
                        .param("sort", "jahr")
                        .param("sort", "monat"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page").value(1))
                .andExpect(jsonPath("$.size").value(2))
                .andExpect(jsonPath("$.totalElements").value(5))
                .andExpect(jsonPath("$.totalPages").value(3))
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[1].id").value(2));
    }
}

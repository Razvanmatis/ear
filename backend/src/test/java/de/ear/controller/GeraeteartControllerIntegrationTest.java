package de.ear.controller;

import de.ear.model.Geraeteart;
import de.ear.services.GeraeteartenService;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class GeraeteartControllerIntegrationTest {

    private final GeraeteartenService service = mock(GeraeteartenService.class);
    private final MockMvc mockMvc = MockMvcBuilders.standaloneSetup(new GeraeteartController(service)).build();

    @Test
    void getGeraeteartenReturnsListFromService() throws Exception {
        Geraeteart first = new Geraeteart();
        first.setNummer(1);
        first.setName("Kühlschrank");
        first.setGruppe("Haushalt");
        first.setKategorie("Elektro");

        Geraeteart second = new Geraeteart();
        second.setNummer(2);
        second.setName("Akkumulator");
        second.setGruppe("Industrie");
        second.setKategorie("Batterie");

        when(service.getGeraetearten()).thenReturn(new Geraeteart[]{first, second});

        mockMvc.perform(get("/geraetearten"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nummer").value(1))
                .andExpect(jsonPath("$[0].name").value("Kühlschrank"))
                .andExpect(jsonPath("$[0].gruppe").value("Haushalt"))
                .andExpect(jsonPath("$[0].kategorie").value("Elektro"))
                .andExpect(jsonPath("$[1].nummer").value(2))
                .andExpect(jsonPath("$[1].name").value("Akkumulator"));
    }
}

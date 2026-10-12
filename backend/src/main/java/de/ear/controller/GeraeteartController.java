package de.ear.controller;

import de.ear.api.GeraeteartenApi;
import de.ear.model.Geraeteart;
import de.ear.services.GeraeteartenService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GeraeteartController implements GeraeteartenApi {

    private final GeraeteartenService service;

    public GeraeteartController(GeraeteartenService service) {
        this.service = service;
    }

    @Override
    public Geraeteart[] getGeraetearten() {
        return service.getGeraetearten();
    }
}

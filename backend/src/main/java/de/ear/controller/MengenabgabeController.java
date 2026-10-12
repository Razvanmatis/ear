package de.ear.controller;

import de.ear.api.MengenabgabenApi;
import de.ear.model.Mengenabgabe;
import de.ear.model.MengenabgabePage;
import de.ear.model.MengenabgabeRequest;
import de.ear.services.MengenabgabeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RestController;
import static de.ear.utilities.MengenabgabeControllerUtilities.createResponse;
import static de.ear.utilities.MengenabgabeControllerUtilities.getPageable;

@RestController
public class MengenabgabeController implements MengenabgabenApi {

    private final MengenabgabeService service;

    public MengenabgabeController(MengenabgabeService service) {
        this.service = service;
    }

    @Override
    public Mengenabgabe createMengenabgabe(MengenabgabeRequest body) {
        return service.createMengenabgabe(body);
    }

    @Override
    public MengenabgabePage getMengenabgaben(Integer page, Integer size, String[] sort) {
        Pageable pageable = getPageable(page, size, sort);
        Page<Mengenabgabe> result = service.getMengenabgaben(pageable);
        return createResponse(result);
    }
}

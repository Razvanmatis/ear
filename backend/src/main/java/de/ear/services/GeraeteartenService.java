package de.ear.services;

import de.ear.exceptions.GeraeteartenSoapRequestFailedException;
import de.ear.mapper.GeraeteartMapper;
import de.ear.model.Geraeteart;
import de.ear.soap.generated.ServiceException;
import lombok.Getter;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class GeraeteartenService {

    private final SoapService soapService;
    private final GeraeteartMapper mapper;

    @Getter
    private List<Integer> existingGerateartnummern = new ArrayList<>();

    public GeraeteartenService(SoapService soapService, GeraeteartMapper mapper) {
        this.soapService = soapService;
        this.mapper = mapper;
    }

    public Geraeteart[] getGeraetearten() {
        try {
            return soapService.getGeraetearten().stream()
                    .map(mapper::toModel)
                    .toArray(Geraeteart[]::new);
        } catch (ServiceException ex) {
            throw new GeraeteartenSoapRequestFailedException(ex.getMessage());
        }
    }

    public boolean geraeteartnummerDoesNotExist(Integer geraeteartnummer) {
        existingGerateartnummern = Arrays.stream(getGeraetearten()).map(Geraeteart::getNummer).toList();
        return !existingGerateartnummern.contains(geraeteartnummer);
    }
}

package de.ear.services;

import de.ear.soap.generated.Geraeteart;
import de.ear.soap.generated.MitteilungsServicePort;
import de.ear.soap.generated.StandardResponse;
import de.ear.soap.generated.ServiceException;
import jakarta.xml.ws.BindingProvider;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
public class SoapService {

    private final MitteilungsServicePort port;

    public SoapService(MitteilungsServicePort port) {
        this.port = port;
    }

    public StandardResponse submitIstInput(
            BigDecimal menge,
            int einheit,
            int jahr,
            int monat,
            int geraeteartnummer,
            int registrierungsnummer,
            boolean wahrheitserklaerung
    ) throws ServiceException {
        Map<String, Object> requestContext = ((BindingProvider) port).getRequestContext();
        requestContext.put(BindingProvider.USERNAME_PROPERTY, "test"); // Test Benutzer-ID
        requestContext.put(BindingProvider.PASSWORD_PROPERTY, "test"); // Test Kennwort
        return port.submitIstInput(
                menge,
                einheit,
                jahr,
                monat,
                geraeteartnummer,
                registrierungsnummer,
                wahrheitserklaerung
        );
    }

    public List<Geraeteart> getGeraetearten() throws ServiceException {
        Map<String, Object> requestContext = ((BindingProvider) port).getRequestContext();
        requestContext.put(BindingProvider.USERNAME_PROPERTY, "test"); // Test Benutzer-ID
        requestContext.put(BindingProvider.PASSWORD_PROPERTY, "test"); // Test Kennwort
        return port.getGeraetearten();
    }
}

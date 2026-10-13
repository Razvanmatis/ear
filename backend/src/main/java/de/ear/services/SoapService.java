package de.ear.services;

import de.ear.soap.generated.Geraeteart;
import de.ear.soap.generated.MitteilungsServicePort;
import de.ear.soap.generated.StandardResponse;
import de.ear.soap.generated.ServiceException;
import de.ear.utilities.BasicAuthCredentials;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.xml.ws.BindingProvider;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.www.BasicAuthenticationConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.server.ResponseStatusException;

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
        BasicAuthCredentials basicAuthCredentials = getBasicAuthCredentials();
        Map<String, Object> requestContext = ((BindingProvider) port).getRequestContext();
        requestContext.put(BindingProvider.USERNAME_PROPERTY, basicAuthCredentials.username());
        requestContext.put(BindingProvider.PASSWORD_PROPERTY, basicAuthCredentials.password());
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
        BasicAuthCredentials basicAuthCredentials = getBasicAuthCredentials();
        Map<String, Object> requestContext = ((BindingProvider) port).getRequestContext();
        requestContext.put(BindingProvider.USERNAME_PROPERTY, basicAuthCredentials.username());
        requestContext.put(BindingProvider.PASSWORD_PROPERTY, basicAuthCredentials.password());
        return port.getGeraetearten();
    }

    private BasicAuthCredentials getBasicAuthCredentials() {
        HttpServletRequest request = ((ServletRequestAttributes)
                RequestContextHolder.currentRequestAttributes()).getRequest();
        Authentication basic = new BasicAuthenticationConverter().convert(request);
        if (basic == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Authentifizierung erforderlich"
            );
        }
        return new BasicAuthCredentials(basic.getName(), (String) basic.getCredentials());
    }
}

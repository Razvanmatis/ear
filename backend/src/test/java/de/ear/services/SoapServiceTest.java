package de.ear.services;

import de.ear.soap.generated.Geraeteart;
import de.ear.soap.generated.MitteilungsServicePort;
import de.ear.soap.generated.ServiceException;
import de.ear.soap.generated.StandardResponse;
import jakarta.xml.ws.BindingProvider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;

class SoapServiceTest {

    @BeforeEach
    void bindBasicAuthRequest() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        String credentials = Base64.getEncoder()
                .encodeToString("test:test".getBytes(StandardCharsets.UTF_8));
        request.addHeader("Authorization", "Basic " + credentials);
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    }

    @AfterEach
    void clearRequestContext() {
        RequestContextHolder.resetRequestAttributes();
    }

    @Test
    void submitIstInputSetsCredentialsAndDelegatesToPort() throws ServiceException {
        MitteilungsServicePort port = mock(MitteilungsServicePort.class, withSettings().extraInterfaces(BindingProvider.class));
        BindingProvider bindingProvider = (BindingProvider) port;
        Map<String, Object> requestContext = new HashMap<>();
        when(bindingProvider.getRequestContext()).thenReturn(requestContext);

        StandardResponse response = new StandardResponse();
        when(port.submitIstInput(
                BigDecimal.valueOf(12.5),
                2,
                2025,
                6,
                7,
                12345678,
                true
        )).thenReturn(response);

        SoapService service = new SoapService(port);

        StandardResponse result = service.submitIstInput(
                BigDecimal.valueOf(12.5),
                2,
                2025,
                6,
                7,
                12345678,
                true
        );

        assertSame(response, result);
        assertEquals("test", requestContext.get(BindingProvider.USERNAME_PROPERTY));
        assertEquals("test", requestContext.get(BindingProvider.PASSWORD_PROPERTY));
        verify(port).submitIstInput(
                BigDecimal.valueOf(12.5),
                2,
                2025,
                6,
                7,
                12345678,
                true
        );
    }

    @Test
    void getGeraeteartenSetsCredentialsAndReturnsSoapList() throws ServiceException {
        MitteilungsServicePort port = mock(MitteilungsServicePort.class, withSettings().extraInterfaces(BindingProvider.class));
        BindingProvider bindingProvider = (BindingProvider) port;
        Map<String, Object> requestContext = new HashMap<>();
        when(bindingProvider.getRequestContext()).thenReturn(requestContext);

        List<Geraeteart> geraetearten = List.of(new Geraeteart());
        when(port.getGeraetearten()).thenReturn(geraetearten);

        SoapService service = new SoapService(port);

        List<Geraeteart> result = service.getGeraetearten();

        assertSame(geraetearten, result);
        assertEquals("test", requestContext.get(BindingProvider.USERNAME_PROPERTY));
        assertEquals("test", requestContext.get(BindingProvider.PASSWORD_PROPERTY));
        verify(port).getGeraetearten();
    }
}

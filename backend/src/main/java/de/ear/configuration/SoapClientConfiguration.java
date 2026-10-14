package de.ear.configuration;

import com.sun.xml.ws.developer.JAXWSProperties;
import de.ear.services.SoapService;
import de.ear.soap.generated.MitteilungsService;
import de.ear.soap.generated.MitteilungsServicePort;
import jakarta.xml.ws.BindingProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SoapClientConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(SoapClientConfiguration.class);

    @Bean
    public MitteilungsServicePort mitteilungsServicePort(
            @Value("${ear.soap.endpoint}") String endpoint) {
        var wsdlLocation = MitteilungsService.class
                .getResource("/wsdl/MitteilungsService.xml");
        if (wsdlLocation == null) {
            LOGGER.error("WSDL resource /wsdl/MitteilungsService.xml wurde nicht gefunden");
            throw new IllegalStateException(
                    "WSDL resource /wsdl/MitteilungsService.xml was not found");
        }

        MitteilungsService service = new MitteilungsService(wsdlLocation);
        MitteilungsServicePort port =
                service.getMitteilungsServicePort();
        BindingProvider bindingProvider = (BindingProvider) port;
        bindingProvider.getRequestContext().put(
                BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
                endpoint
        );
        bindingProvider.getRequestContext().put(
                JAXWSProperties.CONNECT_TIMEOUT, 3_000);

        bindingProvider.getRequestContext().put(
                JAXWSProperties.REQUEST_TIMEOUT, 10_000);
        return port;
    }
}

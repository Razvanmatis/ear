package de.ear.configuration;

import com.sun.xml.ws.developer.JAXWSProperties;
import de.ear.soap.generated.MitteilungsService;
import de.ear.soap.generated.MitteilungsServicePort;
import jakarta.xml.ws.BindingProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SoapClientConfiguration {

    @Bean
    public MitteilungsServicePort mitteilungsServicePort(
            @Value("${ear.soap.endpoint}") String endpoint) {
        MitteilungsService service = new MitteilungsService();
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

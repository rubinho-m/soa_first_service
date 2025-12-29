package com.rubinho.soa_first_service.config;

import com.rubinho.soa_first_service.exceptions.DetailSoapFaultDefinitionExceptionResolver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.server.ServerErrorException;
import org.springframework.ws.soap.server.endpoint.SoapFaultDefinition;
import org.springframework.ws.soap.server.endpoint.SoapFaultMappingExceptionResolver;
import org.springframework.ws.soap.server.endpoint.interceptor.PayloadValidatingInterceptor;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;

import java.util.Properties;

@Configuration(proxyBeanMethods = false)
@Slf4j
public class WebServiceConfig {

    @Bean
    public DefaultWsdl11Definition vehiclesWs(SimpleXsdSchema vehiclesWs) {
        log.info("vehicles ws bean. schema: {}", vehiclesWs);
        DefaultWsdl11Definition wsdl11Definition = new DefaultWsdl11Definition();
        wsdl11Definition.setPortTypeName("VehiclesPort");
        wsdl11Definition.setLocationUri("/ws");
        wsdl11Definition.setTargetNamespace("https://com/rubinho/soa_first_service/generated/vehicles");
        wsdl11Definition.setSchema(vehiclesWs);
        return wsdl11Definition;
    }

    @Bean
    public PayloadValidatingInterceptor validatingInterceptor(SimpleXsdSchema vehiclesWs) {
        PayloadValidatingInterceptor interceptor = new CustomPayloadValidatingInterceptor();
        interceptor.setValidateRequest(true);
        interceptor.setXsdSchema(vehiclesWs);
        return interceptor;
    }

    @Bean
    public SoapFaultMappingExceptionResolver exceptionResolver(){
        SoapFaultMappingExceptionResolver exceptionResolver = new DetailSoapFaultDefinitionExceptionResolver();

        SoapFaultDefinition faultDefinition = new SoapFaultDefinition();
        faultDefinition.setFaultCode(SoapFaultDefinition.SERVER);
        exceptionResolver.setDefaultFault(faultDefinition);

        Properties errorMappings = new Properties();
        errorMappings.setProperty(ServerErrorException.class.getName(), SoapFaultDefinition.SERVER.toString());
//        errorMappings.setProperty(ResponseStatusException.class.getName(), SoapFaultDefinition.CLIENT.toString());
        errorMappings.setProperty(Exception.class.getName(), SoapFaultDefinition.SERVER.toString());

        exceptionResolver.setExceptionMappings(errorMappings);
        exceptionResolver.setOrder(1);
        return exceptionResolver;
    }

}
package com.rubinho.soa_first_service.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.server.EndpointInterceptor;
import org.springframework.ws.soap.server.endpoint.interceptor.PayloadValidatingInterceptor;

import java.util.List;

@Configuration
public class CustomWsConfigurerAdapter extends WsConfigurerAdapter {
    private final PayloadValidatingInterceptor validatingInterceptor;

    public CustomWsConfigurerAdapter(PayloadValidatingInterceptor validatingInterceptor) {
        this.validatingInterceptor = validatingInterceptor;
    }

    @Override
    public void addInterceptors(List<EndpointInterceptor> interceptors) {
        interceptors.add(validatingInterceptor);
    }
}

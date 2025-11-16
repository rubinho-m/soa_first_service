package com.rubinho.soa_first_service.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cloud.client.serviceregistry.AutoServiceRegistrationProperties;
import org.springframework.cloud.consul.discovery.ConsulDiscoveryProperties;
import org.springframework.cloud.consul.serviceregistry.ConsulAutoRegistration;
import org.springframework.cloud.consul.serviceregistry.ConsulAutoServiceRegistration;
import org.springframework.cloud.consul.serviceregistry.ConsulServiceRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

@Configuration
@ConditionalOnProperty(value = "spring.cloud.consul.discovery.enabled", havingValue = "true")
public class ConsulConfiguration {
    private final ConsulServiceRegistry consulServiceRegistry;
    private final ConsulAutoRegistration consulAutoRegistration;

    @Autowired
    public ConsulConfiguration(ConsulServiceRegistry consulServiceRegistry,
                               ConsulAutoRegistration consulAutoRegistration) {
        this.consulServiceRegistry = consulServiceRegistry;
        this.consulAutoRegistration = consulAutoRegistration;
    }

    @Bean
    @ConditionalOnMissingBean
    public ConsulAutoServiceRegistration consulAutoServiceRegistration(
            AutoServiceRegistrationProperties autoServiceRegistrationProperties,
            ConsulDiscoveryProperties consulDiscoveryProperties) {
        return new ConsulAutoServiceRegistration(
                consulServiceRegistry,
                autoServiceRegistrationProperties,
                consulDiscoveryProperties,
                consulAutoRegistration
        );
    }

    @EventListener
    public void handleApplicationReady(ApplicationReadyEvent ignored) {
        System.out.println("Trying to register consul service");
        try {
            consulServiceRegistry.register(consulAutoRegistration);
            System.out.println("Registered consul service successfully");
        } catch (Exception e) {
            System.out.printf("Couldn't register consul service %s%n", e.getMessage());
        }
    }
}

package com.prima.factory.device.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class DeviceServerJacksonConfig
{
    @Bean
    public ObjectMapper objectMapper()
    {
        return new ObjectMapper().findAndRegisterModules();
    }
}

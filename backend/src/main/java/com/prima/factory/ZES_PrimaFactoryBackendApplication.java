package com.prima.factory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
@ComponentScan(
    excludeFilters = @ComponentScan.Filter(
        type = FilterType.REGEX,
        pattern = "com\\.prima\\.factory\\.controller\\.(AuthController|MonitoringController)"))
public class ZES_PrimaFactoryBackendApplication
{
    public static void main(String[] ZES_args)
    {
        SpringApplication.run(ZES_PrimaFactoryBackendApplication.class, ZES_args);
    }
}

package com.prima.factory.device.collector;

import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.MapPropertySource;
import org.springframework.jdbc.core.JdbcTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

class SimulatorEquipmentCollectorContextTest
{
    @Test
    void springCreatesCollectorUsingItsDependencyInjectionConstructor()
    {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext())
        {
            context.getEnvironment().getPropertySources().addFirst(
                new MapPropertySource("test", java.util.Map.of("simulator.random-seed", "123")));
            context.registerBean(JdbcTemplate.class, () -> mock(JdbcTemplate.class));
            context.registerBean(ObjectMapper.class, ObjectMapper::new);
            context.register(SimulatorEquipmentCollector.class);
            context.refresh();

            context.getBean(SimulatorEquipmentCollector.class);
        }
    }
}

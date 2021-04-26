package com.aegis.eventmanagement.scheduler;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class EventSchedulerConfig {
    @Bean
    public Queue deviceEvents() {
        return new Queue("deviceEvents");
    }

}

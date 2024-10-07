package com.bdt.assignment.bdt_final_project.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaConfig {

    @Bean
    public KafkaProducer kafkaProducer() {
        return new KafkaProducer();
    }
}

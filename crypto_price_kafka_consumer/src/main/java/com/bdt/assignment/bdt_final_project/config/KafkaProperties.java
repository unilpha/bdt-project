package com.bdt.assignment.bdt_final_project.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "kafka", ignoreUnknownFields = false)
@Component
@Getter
@Setter
public class KafkaProperties {

    private String bootstrapServers;

    private Template template = new Template();

    @Getter
    @Setter
    public final class Template{

        private String defaultTopic;
    }
}

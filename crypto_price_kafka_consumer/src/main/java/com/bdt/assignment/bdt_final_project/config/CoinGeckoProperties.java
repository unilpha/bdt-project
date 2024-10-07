package com.bdt.assignment.bdt_final_project.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "coin-gecko")
public class CoinGeckoProperties {

    private String url;

    private String authKey;

    private String authHeader;
}

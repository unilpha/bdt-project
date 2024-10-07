package com.bdt.assignment.bdt_final_project.service;

import com.bdt.assignment.bdt_final_project.config.CoinGeckoProperties;
import com.bdt.assignment.bdt_final_project.producer.KafkaProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class CryptoStreamingService {

    private final OkHttpClient client = new OkHttpClient();
    private final CoinGeckoProperties coinGeckoProperties;
    private final KafkaProducer kafkaProducerService;

    public void fetchCryptoData() {
        Request request = new Request.Builder()
                .url(coinGeckoProperties.getUrl())
                .get()
                .addHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .addHeader(coinGeckoProperties.getAuthHeader(), coinGeckoProperties.getAuthKey())
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                String coinData = response.body().string();
                kafkaProducerService.send("aws_crypto2", coinData);
                log.info("Sending Coin data to Kafka: {}", coinData);
            }
        } catch (Exception e) {
            log.error("Error fetching crypto data");
        }
    }


    @Scheduled(fixedRate = 10000)
    public void scheduleFetchCryptoData() {
        log.info("Fetching crypto data from CoinGecko API... @ {}", LocalDateTime.now());
        fetchCryptoData();
    }

}

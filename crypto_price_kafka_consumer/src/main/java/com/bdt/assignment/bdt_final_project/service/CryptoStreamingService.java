package com.bdt.assignment.bdt_final_project.service;

import com.bdt.assignment.bdt_final_project.config.KafkaProducer;
import com.bdt.assignment.bdt_final_project.data.CryptoData;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CryptoStreamingService {

    private final KafkaProducer kafkaProducerService;
    private final ObjectMapper objectMapper = new ObjectMapper();
    public CryptoData fetchCryptoData() {
        try {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url("https://api.coingecko.com/api/v3/simple/price?ids=bitcoin,ethereum&vs_currencies=usd")
                    .get()
                    .addHeader("accept", "application/json")
                    .addHeader("x-cg-pro-api-key", "CG-7MHiQdcM1WgJBnBtBbND8oJR")
                    .build();

            Response response = client.newCall(request).execute();
            if (response.isSuccessful() && response.body() != null) {
                String jsonResponse = response.body().string();
                kafkaProducerService.send("aws_crypto2", jsonResponse);
                return objectMapper.readValue(jsonResponse, CryptoData.class);
            } else {
                System.out.println("response = " + response);
                throw new RuntimeException("Failed to fetch crypto data");
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to fetch crypto data");
        }
    }

    @Scheduled(fixedRate = 10000)
    public void scheduleFetchCryptoData() {
        System.out.println("Fetching crypto data");
        fetchCryptoData();
    }

}

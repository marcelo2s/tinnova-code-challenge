package com.tinnova.vehicles.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class ExchangeService {

    private final RedisTemplate<String, String> redisTemplate;
    private final RestTemplate restTemplate;

    private static final String CACHE_KEY = "USD_BRL";
    private static final long CACHE_TTL_MINUTES = 10;

    public BigDecimal getDolarCurrentValue() {
        String cached = redisTemplate.opsForValue().get(CACHE_KEY);

        if (cached != null) {
            return new BigDecimal(cached);
        }

        try {
            String response = restTemplate.getForObject(
                    "https://economia.awesomeapi.com.br/json/last/USD-BRL",
                    String.class
            );

            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(response);

            BigDecimal dolar = new BigDecimal(
                    node.get("USDBRL").get("bid").asText()
            );

            saveCache(dolar);
            return dolar;
        } catch (Exception e) {
            Map response = restTemplate.getForObject(
                    "https://api.frankfurter.app/latest?from=USD&to=BRL",
                    Map.class
            );

            Map rates = (Map) response.get("rates");
            BigDecimal dolar = new BigDecimal(rates.get("BRL").toString());

            saveCache(dolar);
            return dolar;
        }
    }

    private void saveCache(BigDecimal value) {
        redisTemplate.opsForValue()
                .set(CACHE_KEY, value.toString(), CACHE_TTL_MINUTES, TimeUnit.MINUTES);
    }
}

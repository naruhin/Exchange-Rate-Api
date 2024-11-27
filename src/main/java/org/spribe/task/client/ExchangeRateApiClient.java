package org.spribe.task.client;

import org.spribe.task.domain.dto.ClientResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@Service
public class ExchangeRateApiClient {

    private final String apiUrl;
    private final String accessKey;
    private final RestTemplate restTemplate;

    public ExchangeRateApiClient(@Value("${app.api.url}") String apiUrl,
                                 @Value("${app.api.access-key}") String accessKey,
                                 RestTemplate restTemplate) {
        this.apiUrl = apiUrl;
        this.accessKey = accessKey;
        this.restTemplate = restTemplate;
    }

    public Map<String, Double> fetchExchangeRates(String baseCurrency) {
        String url = buildUrl(baseCurrency);

        try {
            ClientResponseDto response = restTemplate.getForObject(url, ClientResponseDto.class);

            if (response != null && response.isSuccess()) {
                return response.getQuotes();
            }
            throw new RuntimeException("Invalid response from ExchangeRate API");

        } catch (RestClientException e) {
            throw new RuntimeException("Failed to fetch exchange rates from the API: " + e.getMessage(), e);
        }
    }

    public String buildUrl(String baseCurrency) {
        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory(apiUrl);
        return factory.builder()
                .queryParam("access_key", accessKey)
                .queryParam("source", baseCurrency)
                .build()
                .toString();
    }
}

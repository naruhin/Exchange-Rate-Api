package org.spribe.task.client;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

class ExchangeRateApiClientTest {

    @Value("${app.api.url}")
    private String apiUrl = "http://example.com";
    @Value("${app.api.access-key}")
    private String accessKey = "mock-access-key";

    private RestTemplate restTemplate;
    private MockRestServiceServer mockServer;
    private ExchangeRateApiClient exchangeRateApiClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new org.springframework.http.converter.json.MappingJackson2HttpMessageConverter());

        mockServer = MockRestServiceServer.createServer(restTemplate);

        exchangeRateApiClient = new ExchangeRateApiClient(apiUrl, accessKey, restTemplate);
    }

    @Test
    void testFetchExchangeRates_Success() {
        String baseCurrency = "USD";
        String mockUrl = apiUrl + "?access_key=" + accessKey + "&source=" + baseCurrency;

        String jsonResponse = "{" +
                "  \"success\": true," +
                "  \"quotes\": {\"USDGBP\": 0.75, \"USDEUR\": 0.85}" +
                "}";

        mockServer.expect(requestTo(mockUrl))
                .andRespond(withSuccess(jsonResponse, MediaType.APPLICATION_JSON));

        Map<String, Double> actualRates = exchangeRateApiClient.fetchExchangeRates(baseCurrency);

        assertNotNull(actualRates);
        assertEquals(2, actualRates.size());
        assertEquals(0.75, actualRates.get("USDGBP"));
        assertEquals(0.85, actualRates.get("USDEUR"));
    }

    @Test
    void testFetchExchangeRates_InvalidContentType() {
        String baseCurrency = "USD";
        String mockUrl = apiUrl + "?access_key=" + accessKey + "&source=" + baseCurrency;

        mockServer.expect(requestTo(mockUrl))
                .andRespond(withSuccess("<html>Error</html>", MediaType.TEXT_HTML));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            exchangeRateApiClient.fetchExchangeRates(baseCurrency);
        });

        assertTrue(exception.getMessage().contains("Failed to fetch exchange rates from the API"));
    }

    @Test
    void testBuildUrl() {
        String baseCurrency = "USD";
        String expectedUrl = apiUrl + "?access_key=" + accessKey + "&source=" + baseCurrency;

        String actualUrl = exchangeRateApiClient.buildUrl(baseCurrency);

        assertEquals(expectedUrl, actualUrl);
    }
}
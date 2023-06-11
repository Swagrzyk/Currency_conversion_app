package com.example.currency.calculator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class CurrencyController {
    private static final String NBP_API_URL = "http://api.nbp.pl/api/exchangerates/rates/a/";

    @GetMapping("/convert")
    public double convertCurrency(
            @RequestParam("amount") double amount,
            @RequestParam("from") String fromCurrency,
            @RequestParam("to") String toCurrency
    ) {
        double fromRate = getExchangeRate(fromCurrency);
        double toRate = getExchangeRate(toCurrency);

        double convertedAmount = (amount / fromRate) * toRate;
        return convertedAmount;
    }

    private double getExchangeRate(String currencyCode) {
        String apiUrl = NBP_API_URL + currencyCode + "/?format=json";
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<ExchangeRateResponse> responseEntity = restTemplate.getForEntity(apiUrl, ExchangeRateResponse.class);
        ExchangeRateResponse response = responseEntity.getBody();
        if (response != null) {
            return response.getRates().get(0).getMid();
        }
        return 0.0;
    }
}


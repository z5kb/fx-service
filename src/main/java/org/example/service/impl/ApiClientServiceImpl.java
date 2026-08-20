package org.example.service.impl;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.example.model.FxRatesApiClientResponse;
import org.example.service.ApiClientService;
import org.example.service.CachingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

@Service
public class ApiClientServiceImpl implements ApiClientService {

    private final CachingService cachingService;

    @Autowired
    public ApiClientServiceImpl(CachingService cachingService) { this.cachingService = cachingService; }

    @Override
    public synchronized FxRatesApiClientResponse fetch(String currency) {
        // check for cached data
        FxRatesApiClientResponse cachedData = cachingService.getFxRatesData(currency);
        if (cachedData != null) {
            System.out.println("did not call the API, there is valid cached data");
            return cachedData;
        }

        // no cached data, call the API
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(GET_FX_RATES_REQUEST_BASE_URL + currency)).GET().build();
        try {
            HttpResponse<String> responseRaw = client.send(request, HttpResponse.BodyHandlers.ofString());

            // TODO handle API errors and possible parse errors, if any

            // Parse the currency rates
            JsonObject responseJson = JsonParser.parseString(responseRaw.body()).getAsJsonObject();
            Map<String, JsonElement> ratesRaw = responseJson.getAsJsonObject("rates").asMap();
            Map<String, BigDecimal> rates = new HashMap<>(Map.of());
            ratesRaw.forEach((key, value) -> rates.put(key, value.getAsBigDecimal()));

            // Fill the data inside a data class
            FxRatesApiClientResponse response = new FxRatesApiClientResponse();
            response.setSuccessfulRequest(responseJson.get("result").getAsString().equals(REQUEST_WAS_A_SUCCESS_STRING));
            response.setTimeLastUpdateTimestamp(responseJson.get("time_last_update_unix").getAsLong());
            response.setTimeNextUpdateTimestamp(responseJson.get("time_next_update_unix").getAsLong());
            response.setBaseCurrencyCode(responseJson.get("base_code").getAsString());
            response.setCurrencyRates(rates);

            cachingService.cacheFxRatesData(
                    response,
                    response.getTimeNextUpdateTimestamp() - System.currentTimeMillis() / 1000
            );
            return response;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null; // TODO handle this
    }

    // The URL which provides the FX rates
    private static final String GET_FX_RATES_REQUEST_BASE_URL = "https://open.er-api.com/v6/latest/";

    // The keyword which is used to indicate whether the request was successful and is returned inside
    // the request body (not the HTTP status code).
    private static final String REQUEST_WAS_A_SUCCESS_STRING = "success";
}

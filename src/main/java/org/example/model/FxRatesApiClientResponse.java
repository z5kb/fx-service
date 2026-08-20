package org.example.model;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

public class FxRatesApiClientResponse {

    private Boolean successfulRequest; // the result returned from the FX rates provider indicating whether the request was a success
    private Long timeLastUpdateTimestamp; // UNIX timestamp of the last time the data has been updated on the provider's end
    private Long timeNextUpdateTimestamp; // UNIX timestamp of the next time the data will be updated on the provider's end
    private String baseCurrencyCode; // the currency on which the other currency rates are based on (e.g. USD is 1.0, EUR is 1.33 - 1.33 times the value of USD)
    private Map<String, BigDecimal> currencyRates;

    public Boolean getSuccessfulRequest() { return successfulRequest; }

    public void setSuccessfulRequest(Boolean successfulRequest) { this.successfulRequest = successfulRequest; }

    public Long getTimeLastUpdateTimestamp() { return timeLastUpdateTimestamp; }

    public void setTimeLastUpdateTimestamp(Long timeLastUpdateTimestamp) { this.timeLastUpdateTimestamp = timeLastUpdateTimestamp; }

    public Long getTimeNextUpdateTimestamp() { return timeNextUpdateTimestamp; }

    public void setTimeNextUpdateTimestamp(Long timeNextUpdateTimestamp) { this.timeNextUpdateTimestamp = timeNextUpdateTimestamp; }

    public String getBaseCurrencyCode() { return baseCurrencyCode; }

    public void setBaseCurrencyCode(String baseCurrencyCode) { this.baseCurrencyCode = baseCurrencyCode; }

    public Map<String, BigDecimal> getCurrencyRates() { return currencyRates; }

    public void setCurrencyRates(Map<String, BigDecimal> currencyRates) { this.currencyRates = currencyRates; }
}

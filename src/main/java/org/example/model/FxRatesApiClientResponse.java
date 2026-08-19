package org.example.model;

import java.math.BigDecimal;
import java.util.Map;

public class FxRatesApiClientResponse {

    private Boolean successfulRequest;
    private String timeLastUpdateTimestamp;
    private String timeNextUpdateTimestamp;
    private String baseCurrencyCode;
    private Map<String, BigDecimal> currencyRates;

    public Boolean getSuccessfulRequest() { return successfulRequest; }
    public void setSuccessfulRequest(Boolean successfulRequest) { this.successfulRequest = successfulRequest; }
    public String getTimeLastUpdateTimestamp() { return timeLastUpdateTimestamp; }
    public void setTimeLastUpdateTimestamp(String timeLastUpdateTimestamp) { this.timeLastUpdateTimestamp = timeLastUpdateTimestamp; }
    public String getTimeNextUpdateTimestamp() { return timeNextUpdateTimestamp; }
    public void setTimeNextUpdateTimestamp(String timeNextUpdateTimestamp) { this.timeNextUpdateTimestamp = timeNextUpdateTimestamp; }
    public String getBaseCurrencyCode() { return baseCurrencyCode; }
    public void setBaseCurrencyCode(String baseCurrencyCode) { this.baseCurrencyCode = baseCurrencyCode; }
    public Map<String, BigDecimal> getCurrencyRates() { return currencyRates; }
    public void setCurrencyRates(Map<String, BigDecimal> currencyRates) { this.currencyRates = currencyRates; }
}

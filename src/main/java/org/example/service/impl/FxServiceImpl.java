package org.example.service.impl;

import org.example.service.ApiClientService;
import org.example.service.FxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Service
public class FxServiceImpl implements FxService {

    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture<?> pollFuture;

    private final ApiClientService apiClientService;

    @Autowired
    public FxServiceImpl(ApiClientService apiClientService) { this.apiClientService = apiClientService; }

    @Override
    public void prepareFxRatesData() { // TODO make sure this is multi-thread-safe (probably not)
        fetchFxRatesDataAndReschedule();
    }

    private void fetchFxRatesDataAndReschedule() {
        scheduleFxRatesDataFetch(apiClientService.fetch().getTimeNextUpdateTimestamp());
    }

    private void scheduleFxRatesDataFetch(Long timestamp) {
        long delay = timestamp - System.currentTimeMillis() / 1000;
        if (delay <= 0) { // if already expired, run now
            fetchFxRatesDataAndReschedule();
        } else {
            scheduler.schedule(this::fetchFxRatesDataAndReschedule, delay, TimeUnit.SECONDS);
        }
    }
}

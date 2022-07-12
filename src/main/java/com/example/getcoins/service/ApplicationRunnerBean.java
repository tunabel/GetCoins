package com.example.getcoins.service;

import com.example.getcoins.model.Currency;
import com.example.getcoins.repository.CurrencyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Profile("!test")
@Component
@RequiredArgsConstructor
@Slf4j
public class ApplicationRunnerBean implements ApplicationRunner {

    private final CoinServerConsumer coinServerConsumer;
    private final CurrencyRepository currencyRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        log.info("Get currencies from coin server then save for queries");
        List<String> currencies = coinServerConsumer.getCurrencies();
        List<String> currenciesInDB = currencyRepository.findAllCurrencies();

        if (currenciesInDB.isEmpty() || currencies.removeAll(currenciesInDB)) {
            List<Currency> currencyList = new ArrayList<>();
            for (String currencyName : currencies) {
                Currency currency = new Currency();
                currency.setCurrency(currencyName);
                currencyList.add(currency);
            }
            currencyRepository.saveAll(currencyList);
        }
    }
}

package com.example.getcoins.service;

import com.example.getcoins.repository.CurrencyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.verification.VerificationMode;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.DefaultApplicationArguments;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApplicationRunnerBeanTest {

    @Mock
    CoinServerConsumer coinServerConsumer;

    @Mock
    CurrencyRepository currencyRepository;

    @InjectMocks
    ApplicationRunnerBean applicationRunnerBean;

    @Test
    public void givenServerReturnsData_andDbEmpty_whenApplicationRuns_thenSaveCurrenciesIntoDb() throws Exception {

        when(coinServerConsumer.getCurrencies()).thenReturn(List.of("usd", "thb"));
        when(currencyRepository.findAllCurrencies()).thenReturn(List.of());

        applicationRunnerBean.run(new DefaultApplicationArguments());

        verify(currencyRepository, times(1)).saveAll(anyCollection());

    }

}
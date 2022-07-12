package com.example.getcoins.service.impl;

import com.example.getcoins.dto.GetCoinsMarketsDto;
import com.example.getcoins.dto.GetCoinsRequestDto;
import com.example.getcoins.dto.GetCoinsResponseDto;
import com.example.getcoins.exception.InvalidRequestException;
import com.example.getcoins.mapper.CoinMarketsDtoMapper;
import com.example.getcoins.mapper.CoinResponseDtoMapper;
import com.example.getcoins.mapper.CoinsIdDtoMapper;
import com.example.getcoins.model.Coin;
import com.example.getcoins.model.Currency;
import com.example.getcoins.repository.CoinPriceRepository;
import com.example.getcoins.repository.CoinRepository;
import com.example.getcoins.repository.CurrencyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CoinServiceImplTest {

    @InjectMocks
    CoinServiceImpl coinService;

    @Mock
    CurrencyRepository currencyRepository;

    @Mock
    CoinRepository coinRepository;

    @Mock
    CoinPriceRepository coinPriceRepository;

    @Mock
    CoinServerConsumerImpl coinServerConsumer;

    @Spy
    CoinResponseDtoMapper coinsResponseDtoMapper = Mappers.getMapper(CoinResponseDtoMapper.class);

    @Spy
    CoinMarketsDtoMapper coinsMarketsDtoMapper = Mappers.getMapper(CoinMarketsDtoMapper.class);

    @Spy
    CoinsIdDtoMapper coinsIdDtoMapper = Mappers.getMapper(CoinsIdDtoMapper.class);

    private GetCoinsRequestDto requestDto;
    private String curString = "usd";

    @BeforeEach
    private void setup() {
        requestDto = new GetCoinsRequestDto();
        requestDto.setCurrency(curString);
        requestDto.setPage(1);
        requestDto.setPerPage(10);
    }

    @Test
    public void givenCurrencyNotSupported_whenGetCoins_willThrowError() {
        when(currencyRepository.findByCurrency(curString)).thenReturn(Optional.empty());
        assertThrows(InvalidRequestException.class, () -> coinService.getCoins(requestDto));
    }

    @Test
    public void givenServerIsDown_whenGetCoins_willReturnCoinsInDB() {

        Coin coin1 = new Coin();
        coin1.setId("coin1");
        coin1.setName("name1");

        Coin coin2 = new Coin();
        coin2.setId("coin2");
        coin2.setPriceChangePercentage24h(1D);

        Currency currency = new Currency();
        currency.setCurrency(curString);

        when(currencyRepository.findByCurrency(curString)).thenReturn(Optional.of(currency));
        when(coinServerConsumer.isServerDown()).thenReturn(true);
        when(coinRepository.findAllByMarketCapRankBetweenOrderByMarketCapRank(1, 10))
                .thenReturn(List.of(coin1, coin2));

        List<GetCoinsResponseDto> results = coinService.getCoins(requestDto);

        assertEquals(2, results.size());
        assertEquals("name1", results.get(0).getName());
        assertEquals(1D, Double.valueOf(results.get(1).getPriceChangePercentage24h()));
    }

    @Test
    public void givenServerIsAlive_whenGetCoins_willReturnCoinsWithData() {
        Coin coin1 = new Coin();
        coin1.setId("coin1");
        coin1.setName("name1");

        Coin coin2 = new Coin();
        coin2.setId("coin2");
        coin2.setPriceChangePercentage24h(1D);

        GetCoinsMarketsDto dto1 = new GetCoinsMarketsDto();
        dto1.setId("coin1");
        dto1.setCurrentPrice(BigDecimal.ONE);

        GetCoinsMarketsDto dto2 = new GetCoinsMarketsDto();
        dto2.setId("coin2");
        dto2.setCurrentPrice(BigDecimal.ONE);

        Currency currency = new Currency();
        currency.setCurrency(curString);

        when(currencyRepository.findByCurrency(curString)).thenReturn(Optional.of(currency));
        when(coinServerConsumer.isServerDown()).thenReturn(false);
        when(coinServerConsumer.getCoinsMarketsDto(any())).thenReturn(List.of(dto1, dto2));
        when(coinRepository.saveAll(anyCollection())).thenReturn(List.of(coin1, coin2));

        List<GetCoinsResponseDto> results = coinService.getCoins(requestDto);

        assertEquals(2, results.size());
        assertEquals("name1", results.get(0).getName());
        assertEquals(1D, Double.valueOf(results.get(1).getCurrentPrice()));
    }
}
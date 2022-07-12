package com.example.getcoins.service.impl;

import com.example.getcoins.dto.GetCoinsMarketsDto;
import com.example.getcoins.dto.GetCoinsRequestDto;
import com.example.getcoins.dto.GetCoinsResponseDto;
import com.example.getcoins.exception.InvalidRequestException;
import com.example.getcoins.mapper.CoinMarketsDtoMapper;
import com.example.getcoins.mapper.CoinResponseDtoMapper;
import com.example.getcoins.mapper.CoinsIdDtoMapper;
import com.example.getcoins.model.Coin;
import com.example.getcoins.model.CoinPrice;
import com.example.getcoins.model.Currency;
import com.example.getcoins.repository.CoinPriceRepository;
import com.example.getcoins.repository.CoinRepository;
import com.example.getcoins.repository.CurrencyRepository;
import com.example.getcoins.service.CoinService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CoinServiceImpl implements CoinService {

    private final CoinServerConsumerImpl coinServerConsumer;
    private final CurrencyRepository currencyRepository;
    private final CoinRepository coinRepository;
    private final CoinPriceRepository coinPriceRepository;

    private final CoinResponseDtoMapper coinsResponseDtoMapper;
    private final CoinMarketsDtoMapper coinsMarketsDtoMapper;
    private final CoinsIdDtoMapper coinsIdDtoMapper;

    @Override
    public List<GetCoinsResponseDto> getCoins(GetCoinsRequestDto requestDto) {
        log.info("get coins data with query {}", requestDto.toString());
        Currency currency = currencyRepository.findByCurrency(requestDto.getCurrency())
                .orElseThrow(() -> new InvalidRequestException("Currency not supported!"));

        if (coinServerConsumer.isServerDown()) {
            log.info("Coin Server is down!");
            return getCoinsFromDbByMarketCap(requestDto, currency);
        }

        log.info("Get coin markets data for currency {}", requestDto.getCurrency());
        List<GetCoinsMarketsDto> coinsMarketsDtoList = coinServerConsumer.getCoinsMarketsDto(requestDto);
        List<Coin> savedCoinList = saveCoinMarketsDataInDB(coinsMarketsDtoList, currency);
        return coinsResponseDtoMapper.toDtoList(savedCoinList, currency);
    }

    private List<GetCoinsResponseDto> getCoinsFromDbByMarketCap(GetCoinsRequestDto requestDto, Currency currency) {
        int minCapRank = (requestDto.getPage() - 1) * requestDto.getPerPage() + 1;
        int maxCapRank = requestDto.getPage() * requestDto.getPerPage();
        List<Coin> dbCoinList = coinRepository.findAllByMarketCapRankBetweenOrderByMarketCapRank(minCapRank, maxCapRank);
        return coinsResponseDtoMapper.toDtoList(CollectionUtils.isEmpty(dbCoinList) ? List.of(new Coin()) : dbCoinList, currency);
    }

    @Transactional
    public List<Coin> saveCoinMarketsDataInDB(List<GetCoinsMarketsDto> coinsMarketsDtos, Currency currency) {
        List<Coin> coinList = saveCoinListFromCoinsMarketsDtoList(coinsMarketsDtos);
        populateCoinPrice(coinsMarketsDtos, currency, coinList);
        return coinList;
    }

    private List<Coin> saveCoinListFromCoinsMarketsDtoList(List<GetCoinsMarketsDto> coinsMarketsDtos) {
        log.info("build coin data to save into db...");
        List<Coin> coinList = new ArrayList<>();
        for (GetCoinsMarketsDto coinDto : coinsMarketsDtos) {
            Optional<Coin> coinOptional = coinRepository.findById(coinDto.getId());
            Coin coin;
            if (coinOptional.isEmpty() || !StringUtils.hasLength(coinOptional.get().getDescription())) {
                log.info("Coin {} does not exist in db or doesn't have detailed data", coinDto.getId());
                coin =  coinsMarketsDtoMapper.toEntity(coinDto);
                coinsIdDtoMapper.updateEntity(coinServerConsumer.getCoinsIdDto(coinDto.getId()), coin);
            } else {
                log.info("Coin {} exists in db", coinDto.getId());
                coin = coinOptional.get();
            }
            coinList.add(coin);
        }
        return coinRepository.saveAll(coinList);
    }

    private void populateCoinPrice(List<GetCoinsMarketsDto> coinsMarketsDtos, Currency currency, List<Coin> coinList) {
        log.info("Save coin price into db...");
        List<CoinPrice> coinPriceList = new ArrayList<>();
        for (Coin coin: coinList) {
            BigDecimal currentPrice = coinsMarketsDtos.stream().filter(coinDto -> coin.getId().equalsIgnoreCase(coinDto.getId()))
                    .map(GetCoinsMarketsDto::getCurrentPrice)
                    .findFirst()
                    .orElse(BigDecimal.ZERO);
            CoinPrice coinPrice = new CoinPrice();
            coinPrice.setCoin(coin);
            coinPrice.setCurrency(currency);
            coinPrice.setCurrentPrice(currentPrice);
            coinPriceList.add(coinPrice);
            coin.getCoinPrices().add(coinPrice);
        }
        coinPriceRepository.saveAll(coinPriceList);
    }
}

package com.example.getcoins.service.impl;

import com.example.getcoins.configuration.CoinServerUrl;
import com.example.getcoins.dto.GetCoinsIdDto;
import com.example.getcoins.dto.GetCoinsMarketsDto;
import com.example.getcoins.dto.GetCoinsRequestDto;
import com.example.getcoins.service.CoinServerConsumer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

import static com.example.getcoins.constant.Constants.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class CoinServerConsumerImpl implements CoinServerConsumer {

    private final WebClient webClient;
    private final CoinServerUrl coinServerUrl;
    @Override
    public boolean isServerDown() {
        try {
            return Boolean.FALSE.equals(webClient.get()
                    .uri(coinServerUrl.getPing())
                    .exchangeToMono(response -> {
                                if (response.statusCode().is2xxSuccessful()) {
                                    return Mono.just(true);
                                } else {
                                    return Mono.just(false);
                                }
                            }
                    )
                    .block());
        } catch (Exception ex) {
            log.error("Get data from server error: {}", ex.getMessage());
            return true;
        }
    }

    @Override
    public List<GetCoinsMarketsDto> getCoinsMarketsDto(GetCoinsRequestDto requestDto) {
        try {
            return webClient.get()
                    .uri(uriBuilder -> uriBuilder.path(coinServerUrl.getCoinsMarkets())
                            .queryParam(VS_CURRENCY, requestDto.getCurrency())
                            .queryParam(PAGE, requestDto.getPage())
                            .queryParam(PER_PAGE, requestDto.getPerPage())
                            .build()
                    )
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<List<GetCoinsMarketsDto>>() {})
                    .block();
        } catch (Exception ex) {
            log.error("Get data from server error: {}", ex.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public List<String> getCurrencies() {
        try {
            return webClient.get()
                    .uri(coinServerUrl.getSupportedCurrencies())
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<List<String>>() {})
                    .block();
        } catch (Exception ex) {
            log.error("Get data from server error: {}", ex.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public GetCoinsIdDto getCoinsIdDto(String id) {
        try {
            return webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path(coinServerUrl.getCoinsId())
                            .build(id))
                    .retrieve()
                    .bodyToMono(GetCoinsIdDto.class)
                    .block();
        } catch (Exception ex) {
            log.error("Get data from server error {}", ex.getMessage());
            return new GetCoinsIdDto();
        }
    }
}

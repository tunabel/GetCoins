package com.example.getcoins.service;

import com.example.getcoins.dto.GetCoinsIdDto;
import com.example.getcoins.dto.GetCoinsMarketsDto;
import com.example.getcoins.dto.GetCoinsRequestDto;

import java.util.List;

public interface CoinServerConsumer {
    boolean isServerDown();

    List<GetCoinsMarketsDto> getCoinsMarketsDto(GetCoinsRequestDto requestDto);

    List<String> getCurrencies();

    GetCoinsIdDto getCoinsIdDto(String id);
}

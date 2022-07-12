package com.example.getcoins.service;

import com.example.getcoins.dto.GetCoinsRequestDto;
import com.example.getcoins.dto.GetCoinsResponseDto;

import java.util.List;

public interface CoinService {
    List<GetCoinsResponseDto> getCoins(GetCoinsRequestDto requestDto);
}

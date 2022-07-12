package com.example.getcoins.mapper;

import com.example.getcoins.dto.GetCoinsResponseDto;
import com.example.getcoins.model.Coin;
import com.example.getcoins.model.CoinPrice;
import com.example.getcoins.model.Currency;
import org.mapstruct.*;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CoinResponseDtoMapper {
    @Mapping(source = "priceChangePercentage24h", target = "priceChangePercentage24h", numberFormat = "#.##")
    Coin toEntity(GetCoinsResponseDto dto);


    @Mapping(source = "coin.priceChangePercentage24h", target = "priceChangePercentage24h", numberFormat = "#.##")
    @Mapping(source = "coin.coinPrices", target = "currentPrice", numberFormat = "#.##")
    GetCoinsResponseDto toDto(Coin coin, @Context Currency currency);

    default BigDecimal map(List<CoinPrice> coinPriceList, @Context Currency currency) {
        return coinPriceList != null && !coinPriceList.isEmpty() ?
                coinPriceList.stream().filter(coinPrice -> ObjectUtils.nullSafeEquals(coinPrice.getCurrency(), currency))
                        .findAny().orElse(new CoinPrice())
                        .getCurrentPrice() : BigDecimal.ZERO;
    }

    List<GetCoinsResponseDto> toDtoList(List<Coin> coinList, @Context Currency currency);
}

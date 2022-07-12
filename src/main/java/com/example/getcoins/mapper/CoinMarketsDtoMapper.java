package com.example.getcoins.mapper;

import com.example.getcoins.dto.GetCoinsMarketsDto;
import com.example.getcoins.model.Coin;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CoinMarketsDtoMapper {
    Coin toEntity(GetCoinsMarketsDto dto);

    List<Coin> toEntityList(List<GetCoinsMarketsDto> dtoList);

    GetCoinsMarketsDto toDto(Coin coin);

    List<GetCoinsMarketsDto> toDtoList(List<Coin> coinList);
}

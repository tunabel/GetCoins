package com.example.getcoins.mapper;

import com.example.getcoins.dto.GetCoinsIdDto;
import com.example.getcoins.dto.TickerDto;
import com.example.getcoins.model.Coin;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CoinsIdDtoMapper {

    @Mapping(source = "description.en", target = "description")
    @Mapping(source = "tickers", target = "tradeUrl")
    void updateEntity(GetCoinsIdDto dto, @MappingTarget Coin coin);

    default String map(List<TickerDto> tickerDtos) {
        return tickerDtos != null && !tickerDtos.isEmpty() ? tickerDtos.get(0).getTradeUrl() : "";
    }
}

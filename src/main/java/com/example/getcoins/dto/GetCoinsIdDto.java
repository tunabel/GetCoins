package com.example.getcoins.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetCoinsIdDto {
    String id;
    String name;
    String symbol;
    @JsonProperty("price_change_percentage_24h")
    Double priceChangePercentage24h;
    BigDecimal currentPrice;
    DescriptionDto description;
    List<TickerDto> tickers;
}

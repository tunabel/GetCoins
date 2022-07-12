package com.example.getcoins.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonPropertyOrder({ "image", "symbol", "name", "price_change_percentage_24h", "current_price", "description", "trade_url" })
public class GetCoinsResponseDto {

    private String image;

    private String symbol;

    private String name;

    @JsonProperty("price_change_percentage_24h")
    private String priceChangePercentage24h;

    private String currentPrice;

    private String description;

    private String tradeUrl;
}

package com.example.getcoins.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetCoinsMarketsDto {
     String id;
     String symbol;
     String name;
     String image;
     @JsonProperty("price_change_percentage_24h")
     Double priceChangePercentage24h;
     BigDecimal currentPrice;
     Integer marketCapRank;
}

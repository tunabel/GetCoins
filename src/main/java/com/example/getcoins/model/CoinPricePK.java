package com.example.getcoins.model;

import lombok.*;

import java.io.Serializable;

@EqualsAndHashCode
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CoinPricePK implements Serializable {

    private String coin;

    private Integer currency;
}

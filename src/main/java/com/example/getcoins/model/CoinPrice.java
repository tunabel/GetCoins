package com.example.getcoins.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "coin_price")
@Getter
@Setter
@IdClass(CoinPricePK.class)
public class CoinPrice {


    @Id
    @ManyToOne(optional = false)
    @JoinColumn(name = "currency_id", nullable = false)
    private Currency currency;

    @Id
    @ManyToOne(optional = false)
    @JoinColumn(name = "coin_id")
    private Coin coin;


    @Column(name = "current_price", precision = 19, scale = 2)
    private BigDecimal currentPrice = BigDecimal.ZERO;
}
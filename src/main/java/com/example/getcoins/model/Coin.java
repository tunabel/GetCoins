package com.example.getcoins.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "coin")
@Getter
@Setter
public class Coin {
    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "symbol")
    private String symbol;

    @Column(name = "image")
    private String image;

    @Column(name = "name")
    private String name;

    @Column(name = "price_change_percentage_24h", precision = 19, scale = 2)
    private Double priceChangePercentage24h;

    @OneToMany(mappedBy = "coin", orphanRemoval = true)
    private List<CoinPrice> coinPrices = new ArrayList<>();

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "trade_url")
    private String tradeUrl;

    @Column(name = "market_cap_rank")
    private Integer marketCapRank;
}
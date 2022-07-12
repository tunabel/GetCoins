package com.example.getcoins.repository;

import com.example.getcoins.model.CoinPrice;
import com.example.getcoins.model.CoinPricePK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoinPriceRepository extends JpaRepository<CoinPrice, CoinPricePK> {
}
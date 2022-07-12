package com.example.getcoins.repository;

import com.example.getcoins.model.Coin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CoinRepository extends JpaRepository<Coin, String> {

    List<Coin> findAllByMarketCapRankBetweenOrderByMarketCapRank(int min, int max);

}
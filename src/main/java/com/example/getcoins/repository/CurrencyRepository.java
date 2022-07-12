package com.example.getcoins.repository;

import com.example.getcoins.model.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CurrencyRepository extends JpaRepository<Currency, Integer> {

    @Query("select c.currency from Currency c")
    List<String> findAllCurrencies();

    Optional<Currency> findByCurrency(String currency);
}
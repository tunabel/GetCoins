CREATE TABLE IF NOT EXISTS coin_price
(
    currency_id   INTEGER      NOT NULL,
    coin_id       VARCHAR(255) NOT NULL,
    current_price DECIMAL(19, 2),
    CONSTRAINT pk_coin_price PRIMARY KEY (currency_id, coin_id)
);

ALTER TABLE coin_price
    ADD CONSTRAINT FK_COIN_PRICE_ON_COIN FOREIGN KEY (coin_id) REFERENCES coin (id);

ALTER TABLE coin_price
    ADD CONSTRAINT FK_COIN_PRICE_ON_CURRENCY FOREIGN KEY (currency_id) REFERENCES currency (id);

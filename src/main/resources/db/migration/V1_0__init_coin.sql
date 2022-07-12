CREATE TABLE IF NOT EXISTS coin
(
    id                          VARCHAR(255) NOT NULL,
    symbol                      VARCHAR(255),
    image                       VARCHAR(255),
    name                        VARCHAR(255),
    price_change_percentage_24h DOUBLE PRECISION,
    description                 TEXT,
    trade_url                   VARCHAR(255),
    market_cap_rank             INTEGER,
    CONSTRAINT pk_coin PRIMARY KEY (id)
);
# JAVA (back-end) Developer Recruitment Assessment

## Table of contents

* [General info](#general-info)
* [Technologies](#technologies)
* [Setup](#setup)
* [Requirements](#requirements)
* [URL](#url)
* [Explanations](#explanation)

## General Info

This is a project to develop a web service to get and transform data from Coin-Gecko Server

## Technologies

* Java 1.11
* Spring Boot 2.7.1
* PostgreSQL 42.4.0

## Setup

**Prerequisites:**

- Running Postgres DB at port 5432. Config username/password/database in application.yml

**Run:**

- Open the project in IDE and run the project
- Select 'dev' as active profile

## URL

- Please visit [GetCoins.html](http://localhost:8080/getcoins.html) to access the documentation

## Requirements

- Create API [get_coins] to query data from coingecko server and return response with predefined structure
- This API must support in case of the api.coingecko.com is unreachable (network error, site is down, etc.)

## Explanations

- The backend server's main feature is reformat the data provided by coin gecko.
  It is also saving coins and currencies from coin server to later can query them again in case the coin server can't be
  reached. To support this, when booting up, the service will call to coin server to load all supported currencies. When
  coin server is down, the service can validate the requested currency and return existing coin data if it was loaded before.
- In case the database doesn't have data, a dummy object will be returned.
- Also, other libraries were included, such as
    - Flyway for Database migration, so that we can update database schema in a secure way. That's why
      _spring.jpa.hibernate.ddl-auto_ is set at _create_ to leverage this dependency.
    - WebClient to support asynchronous web requests
    - MapStruct to facilitate object mapping
    - Spring OpenApi is included to generate API documentation for the web service.


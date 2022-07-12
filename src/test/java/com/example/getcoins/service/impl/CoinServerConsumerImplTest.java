package com.example.getcoins.service.impl;

import com.example.getcoins.configuration.CoinServerUrl;
import com.example.getcoins.dto.GetCoinsIdDto;
import com.example.getcoins.dto.GetCoinsMarketsDto;
import com.example.getcoins.dto.GetCoinsRequestDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CoinServerConsumerImplTest {

    public static MockWebServer mockBackEnd;

    @BeforeAll
    static void setUp() throws IOException {
        mockBackEnd = new MockWebServer();
        mockBackEnd.start();

    }

    @AfterAll
    static void tearDown() throws IOException {
        mockBackEnd.shutdown();
    }

    @BeforeEach
    void initialize() {
        CoinServerUrl coinServerUrl = new CoinServerUrl();
        coinServerUrl.setApi(mockBackEnd.url("/").url().toString());
        coinServerConsumer = new CoinServerConsumerImpl(
                WebClient.builder().baseUrl(coinServerUrl.getApi()).build(), coinServerUrl);
    }

    CoinServerConsumerImpl coinServerConsumer;

    @Test
    void givenServerIsDown_whenQueryPing_thenReturn500() {
        mockBackEnd.enqueue(
                new MockResponse().setResponseCode(500)
        );
        boolean isServerDown = coinServerConsumer.isServerDown();
        assertTrue(isServerDown);
    }

    @Test
    void givenServerIsAlive_whenQueryPing_thenReturn200() {
        mockBackEnd.enqueue(
                new MockResponse().setResponseCode(200)
        );
        boolean isServerDown = coinServerConsumer.isServerDown();

        assertFalse(isServerDown);
    }

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void givenQueryDataIsCorrect_whenQueryCoinsMarketsData_thenReturnArrayList() throws Exception {
        GetCoinsMarketsDto  dto = new GetCoinsMarketsDto();
        dto.setId("coin");
        mockBackEnd.enqueue(
                new MockResponse().setResponseCode(200)
                        .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setBody(objectMapper.writeValueAsString(new GetCoinsMarketsDto[] {dto}))
        );
        List<GetCoinsMarketsDto> results = coinServerConsumer.getCoinsMarketsDto(new GetCoinsRequestDto());

        assertEquals(1, results.size());
        assertEquals("coin", results.get(0).getId());
    }

    @Test
    void givenQueryDataIsNotCorrect_whenQueryCoinsMarketsData_thenReturnEmptyArrayList() throws Exception {
        mockBackEnd.enqueue(
                new MockResponse().setResponseCode(500)
        );
        List<GetCoinsMarketsDto> results = coinServerConsumer.getCoinsMarketsDto(new GetCoinsRequestDto());

        assertEquals(0, results.size());
    }

    @Test
    void givenServerIsRunning_whenQueryCurrenciesData_thenReturnListString() throws Exception {
        String[] currencies = new String[] {"coin1", "coin2"};

        mockBackEnd.enqueue(
                new MockResponse().setResponseCode(200)
                        .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setBody(objectMapper.writeValueAsString(currencies))
        );
        List<String> results = coinServerConsumer.getCurrencies();

        assertEquals(2, results.size());
        assertEquals("coin1", results.get(0));
    }

    @Test
    void givenServerIsNotRunning_whenQueryCurrenciesData_thenReturnEmptyListString() throws Exception {
        mockBackEnd.enqueue(
                new MockResponse().setResponseCode(500)
        );
        List<String> results = coinServerConsumer.getCurrencies();

        assertEquals(0, results.size());
    }

    @Test
    void givenServerIsRunning_whenQueryCoinsIdData_thenReturnCoinData() throws Exception {
        String id = "coin1";
        GetCoinsIdDto dto = new GetCoinsIdDto();
        dto.setId(id);

        mockBackEnd.enqueue(
                new MockResponse().setResponseCode(200)
                        .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setBody(objectMapper.writeValueAsString(dto))
        );
        GetCoinsIdDto results = coinServerConsumer.getCoinsIdDto(id);

        assertEquals(id, results.getId());
    }

    @Test
    void givenServerIsNotRunning_whenQueryCoinsIdData_thenReturnEmptyCoinData() throws Exception {
        String id = "coin1";
        mockBackEnd.enqueue(
                new MockResponse().setResponseCode(500)
        );
        GetCoinsIdDto results = coinServerConsumer.getCoinsIdDto(id);

        assertNull(results.getId());
    }








}
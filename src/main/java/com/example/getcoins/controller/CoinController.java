package com.example.getcoins.controller;

import com.example.getcoins.dto.GetCoinsRequestDto;
import com.example.getcoins.dto.GetCoinsResponseDto;
import com.example.getcoins.service.CoinService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/coins")
@RequiredArgsConstructor
@Tag(name = "Coin Controller", description = "Coins-related functions")
public class CoinController {

    private final CoinService coinService;

    @Operation(summary = "Get Coins data from Coin Server")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Coins Data are found", content = {@Content(mediaType = "application/json"), }),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "500", description = "Server internal error", content = {@Content(mediaType = "application/json")})
    })
    @GetMapping("get_coins")
    public ResponseEntity<?> getCoins(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Request body to get coin data", required = true,
            content = @Content(schema=@Schema(implementation = GetCoinsRequestDto.class)))
            @Valid @RequestBody GetCoinsRequestDto requestDto) {
        return ResponseEntity.ok(coinService.getCoins(requestDto));
    }
}

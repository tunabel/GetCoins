package com.example.getcoins.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.util.StringUtils;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Locale;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@ToString
public class GetCoinsRequestDto {

    @NotNull(message = "Currency must have value")
    String currency;

    @Min(value = 1, message = "Page min value is 1")
    @Max(value = 100, message = "Page max value is 100")
    int page = 0;

    @Min(value = 1, message = "Per_page min value is 1")
    @Max(value = 250, message = "Per_page max value is 250")
    int perPage;

    public void setCurrency(String currency) {
        this.currency = currency.toLowerCase(Locale.ROOT);
    }
}

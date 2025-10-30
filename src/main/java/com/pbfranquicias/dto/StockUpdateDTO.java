package com.pbfranquicias.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StockUpdateDTO {
    @NotNull
    private Integer stock;
}

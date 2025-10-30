package com.pbfranquicias.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProductCreateDTO {
    @NotBlank
    private String name;

    @NotNull
    private Integer stock;
}
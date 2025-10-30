package com.pbfranquicias.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class BranchCreateDTO {
    @NotBlank
    private String name;
}

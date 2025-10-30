package com.pbfranquicias.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.util.List;

@Data
public class FranchiseCreateDTO {
    @NotBlank
    private String name;
}

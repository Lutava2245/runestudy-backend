package com.fatec.runestudy.domain.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SkillRequestDTO {

    @Valid
    @NotBlank
    @Size(min = 3, max = 100)
    private String name;

    @Valid
    @NotNull
    private int difficult;
    
}

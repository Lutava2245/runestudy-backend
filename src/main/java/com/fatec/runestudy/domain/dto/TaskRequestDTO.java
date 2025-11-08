package com.fatec.runestudy.domain.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class TaskRequestDTO {
    
    @Valid
    @NotBlank
    @Size(min = 3, max = 100)
    private String title;

    @Valid
    @Size(min = 3, max = 255)
    private String description;

    @Valid
    @NotNull
    private int taskXP;

    @Valid
    @NotNull
    private Long skillId;

}

package com.fatec.runestudy.domain.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BlockTaskDTO {
    
    @Valid
    @NotNull
    private boolean block;

}

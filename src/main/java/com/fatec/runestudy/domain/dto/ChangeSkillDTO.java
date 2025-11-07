package com.fatec.runestudy.domain.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ChangeSkillDTO {

    @Valid
    @NotBlank
    private Long skillId;

}
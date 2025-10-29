package com.fatec.runestudy.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class SkillResponseDTO {
    
    private String name;

    private int points;

    private int difficult;

    private int totalXP;

}

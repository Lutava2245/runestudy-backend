package com.fatec.runestudy.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class UserResponseDTO {

    private String name;
    
    private String nickname;

    private String email;

    private int totalXP;

    private int totalCoins;
}

package com.fatec.runestudy.domain.dto.response;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class UserResponse {

    private Long id;

    private String name;
    
    private String nickname;

    private String email;

    private String currentAvatar;

    private int level;

    private LocalDate createdAt;

    private int totalXP;

    private int totalCoins;
    
}

package com.fatec.runestudy.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class TaskResponseDTO {
    
    private String title;

    private String description;

    private boolean block;

    private String status;

    private int task_xp;

}

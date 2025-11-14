package com.fatec.runestudy.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class TaskResponse {

    private Long id;
    
    private String title;

    private String description;

    private String status;

    private boolean block;

    private int task_xp;

}

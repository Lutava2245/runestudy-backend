package com.fatec.runestudy.domain.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ChangePasswordDTO {
    
    @Valid
    private String currentPassword;

    @Valid
    @NotBlank
    private String newPassword;

}

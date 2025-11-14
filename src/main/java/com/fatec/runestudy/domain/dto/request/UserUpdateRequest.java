package com.fatec.runestudy.domain.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserUpdateRequest {
    
    @Valid
    @NotBlank
    @Size(min = 2, max = 100)
    private String name;

    @Valid
    @NotBlank
    @Size(min = 2, max = 30)
    private String nickname;

    @Email
    @Valid
    @NotBlank
    @Size(min = 5, max = 100)
    private String email;

}

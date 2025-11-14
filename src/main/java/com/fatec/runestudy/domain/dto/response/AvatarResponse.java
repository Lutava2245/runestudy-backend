package com.fatec.runestudy.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class AvatarResponse {

    private Long id;

    private String name;

    private String icon;

    private int price;

    private boolean owned;

}

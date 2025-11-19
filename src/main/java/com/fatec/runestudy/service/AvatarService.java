package com.fatec.runestudy.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.fatec.runestudy.domain.dto.response.AvatarResponse;
import com.fatec.runestudy.domain.model.Avatar;
import com.fatec.runestudy.domain.model.User;

@Service
public interface AvatarService {
    
    AvatarResponse convertAvatarToDTO(Avatar avatar, User user);

    boolean isOwned(String name, Long userId);

    List<AvatarResponse> getAllAvatars(User user);

}

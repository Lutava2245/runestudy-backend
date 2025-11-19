package com.fatec.runestudy.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fatec.runestudy.domain.dto.response.AvatarResponse;
import com.fatec.runestudy.domain.model.Avatar;
import com.fatec.runestudy.domain.model.User;
import com.fatec.runestudy.domain.repository.AvatarRepository;
import com.fatec.runestudy.domain.repository.UserRepository;
import com.fatec.runestudy.exception.ResourceNotFoundException;
import com.fatec.runestudy.service.AvatarService;

@Service
public class AvatarServiceImpl implements AvatarService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AvatarRepository avatarRepository;

    @Override
    public AvatarResponse convertAvatarToDTO(Avatar avatar, User user) {
        boolean isOwned = user.getOwnedAvatars().contains(avatar);

        return new AvatarResponse(
            avatar.getId(),
            avatar.getName(),
            avatar.getIcon(),
            avatar.getPrice(),
            isOwned);
    }

    @Override
    public boolean isOwned(String name, Long userId) {
        Avatar avatar = avatarRepository.findByName(name)
            .orElseThrow(() -> new ResourceNotFoundException("Erro: Avatar não encontrado."));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Erro: Usuário não encontrado."));

        return user.getOwnedAvatars().contains(avatar);
    }
    
    @Override
    public List<AvatarResponse> getAllAvatars(User user) {
        List<Avatar> avatars = avatarRepository.findAll();

        if (avatars.isEmpty()) {
            throw new ResourceNotFoundException("Erro: Nenhum avatar encontrado.");
        }

        return avatars.stream()
            .map(avatar -> convertAvatarToDTO(avatar, user))
            .collect(Collectors.toList());
    }

}

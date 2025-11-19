package com.fatec.runestudy.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fatec.runestudy.domain.model.Avatar;
import com.fatec.runestudy.domain.model.User;
import com.fatec.runestudy.domain.repository.AvatarRepository;
import com.fatec.runestudy.domain.repository.UserRepository;
import com.fatec.runestudy.exception.ResourceNotFoundException;
import com.fatec.runestudy.exception.InsufficientCoinsException;
import com.fatec.runestudy.service.StoreService;

@Service
public class StoreServiceImpl implements StoreService {

    @Autowired
    private AvatarRepository avatarRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    @Override
    public void buyAvatar(User user, Long avatarId) {
        Avatar avatar = avatarRepository.findById(avatarId)
            .orElseThrow(() -> new ResourceNotFoundException("Erro: Nenhum avatar encontrado."));

        if (user.getTotalCoins() < avatar.getPrice()) {
            throw new InsufficientCoinsException();
        }

        user.setTotalCoins(user.getTotalCoins() - avatar.getPrice());
        user.getOwnedAvatars().add(avatar);
        
        userRepository.save(user);
    }
    
}

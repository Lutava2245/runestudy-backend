package com.fatec.runestudy.service.impl;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fatec.runestudy.domain.model.Avatar;
import com.fatec.runestudy.domain.model.Reward;
import com.fatec.runestudy.domain.model.User;
import com.fatec.runestudy.domain.repository.AvatarRepository;
import com.fatec.runestudy.domain.repository.RewardRepository;
import com.fatec.runestudy.domain.repository.UserRepository;
import com.fatec.runestudy.exception.ResourceNotFoundException;
import com.fatec.runestudy.exception.DuplicateResourceException;
import com.fatec.runestudy.exception.InsufficientCoinsException;
import com.fatec.runestudy.service.StoreService;

@Service
public class StoreServiceImpl implements StoreService {

    @Autowired
    private AvatarRepository avatarRepository;

    @Autowired
    private RewardRepository rewardRepository;

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

        boolean isOwned = user.getOwnedAvatars().stream()
                .anyMatch(userAvatar -> avatar.getIconName().equals(userAvatar.getIconName()));

        if (isOwned) {
            throw new DuplicateResourceException("Erro: Recompensa já foi resgatada.");
        }

        Set<Avatar> ownedAvatars = user.getOwnedAvatars();
        ownedAvatars.add(avatar);

        user.setTotalCoins(user.getTotalCoins() - avatar.getPrice());
        user.setOwnedAvatars(ownedAvatars);

        userRepository.save(user);
    }

    @Transactional
    @Override
    public void claimReward(Long rewardId) {
        Reward reward = rewardRepository.findById(rewardId)
                .orElseThrow(() -> new ResourceNotFoundException("Erro: Recompensa não encontrada."));
        User user = reward.getUser();
                
        if (user.getTotalCoins() < reward.getPrice()) {
            throw new InsufficientCoinsException();
        }

        reward.setRedeemed(true);
        user.setTotalCoins(user.getTotalCoins() - reward.getPrice());

        userRepository.save(user);
        rewardRepository.save(reward);
    }

}

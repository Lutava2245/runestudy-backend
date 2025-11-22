package com.fatec.runestudy.service;

import org.springframework.stereotype.Service;

import com.fatec.runestudy.domain.model.User;

@Service
public interface StoreService {

    void buyAvatar(User user, Long avatarId);

    void claimReward(Long rewardId);
    
}

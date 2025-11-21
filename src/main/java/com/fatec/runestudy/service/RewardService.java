package com.fatec.runestudy.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.fatec.runestudy.domain.dto.request.RewardRequest;
import com.fatec.runestudy.domain.dto.response.RewardResponse;
import com.fatec.runestudy.domain.model.Reward;
import com.fatec.runestudy.domain.model.User;

@Service
public interface RewardService {
    
    RewardResponse convertToDTO(Reward reward);

    boolean isOwner(Long rewardId, Long userId);

    RewardResponse getById(Long id);

    List<RewardResponse> getAll();

    List<RewardResponse> getByUserId(Long id);

    void createReward(RewardRequest request, User user);

    void updateRewardById(Long id, RewardRequest request);

    void deleteRewardById(Long id);

}

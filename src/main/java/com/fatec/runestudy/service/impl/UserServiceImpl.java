package com.fatec.runestudy.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fatec.runestudy.domain.dto.UserResponseDTO;
import com.fatec.runestudy.domain.model.User;
import com.fatec.runestudy.domain.repository.UserRepository;
import com.fatec.runestudy.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserResponseDTO convertToDTO(User user) {
        return new UserResponseDTO(
            user.getName(),
            user.getNickname(),
            user.getEmail(),
            user.getTotalXP(),
            user.getTotalCoins());
    }

    @Override
    public UserResponseDTO getById(Long id) {
        User user = userRepository.findById(id).orElse(null);

        if (user == null) {
            return null;
        }

        return convertToDTO(user);
    }

    @Override
    public List<UserResponseDTO> getAll() {
        List<User> users = userRepository.findAll();

        if (users.isEmpty()) {
            return new ArrayList<>();
        }

        return users.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
}

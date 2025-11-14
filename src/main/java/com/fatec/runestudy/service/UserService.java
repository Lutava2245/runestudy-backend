package com.fatec.runestudy.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.fatec.runestudy.domain.dto.request.ChangePasswordRequest;
import com.fatec.runestudy.domain.dto.request.UserCreateRequest;
import com.fatec.runestudy.domain.dto.request.UserUpdateRequest;
import com.fatec.runestudy.domain.dto.response.UserResponse;
import com.fatec.runestudy.domain.model.Skill;
import com.fatec.runestudy.domain.model.User;

@Service
public interface UserService {

    UserResponse convertToDTO(User user);

    Skill createDefaultSkill(User user);

    UserResponse getById(Long id);

    List<UserResponse> getAll();

    UserResponse createUser(UserCreateRequest requestDTO);

    UserResponse updateUserById(Long id, UserUpdateRequest requestDTO);

    void changePassword(Long id, ChangePasswordRequest requestDTO);

    void deleteUserById(Long id);

}

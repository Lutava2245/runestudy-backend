package com.fatec.runestudy.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.fatec.runestudy.domain.dto.UserCreateDTO;
import com.fatec.runestudy.domain.dto.UserResponseDTO;
import com.fatec.runestudy.domain.dto.UserUpdateDTO;
import com.fatec.runestudy.domain.model.User;

@Service
public interface UserService {

    UserResponseDTO convertToDTO(User user);

    UserResponseDTO getById(Long id);

    List<UserResponseDTO> getAll();

    UserResponseDTO createUser(UserCreateDTO requestDTO);

    UserResponseDTO updateUserById(Long id, UserUpdateDTO requestDTO);

}

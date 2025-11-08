package com.fatec.runestudy.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.fatec.runestudy.domain.dto.ChangePasswordDTO;
import com.fatec.runestudy.domain.dto.UserCreateDTO;
import com.fatec.runestudy.domain.dto.UserResponseDTO;
import com.fatec.runestudy.domain.dto.UserUpdateDTO;
import com.fatec.runestudy.domain.model.Role;
import com.fatec.runestudy.domain.model.Skill;
import com.fatec.runestudy.domain.model.User;
import com.fatec.runestudy.domain.repository.RoleRepository;
import com.fatec.runestudy.domain.repository.SkillRepository;
import com.fatec.runestudy.domain.repository.UserRepository;
import com.fatec.runestudy.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private SkillRepository skillRepository;

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
    public Skill createDefaultSkill(User user) {
        Skill skill = new Skill();
        skill.setName("Habilidade Padrão");
        skill.setDifficult(1);
        skill.setUser(user);
        return skill;
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

    @Override
    public UserResponseDTO createUser(UserCreateDTO requestDTO) {
        if (userRepository.existsByEmailOrNickname(requestDTO.getEmail(), requestDTO.getNickname())) {
            return null;
        }

        String hashedPassword = passwordEncoder.encode(requestDTO.getPassword());
        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Erro: Role padrão não encontrado."));

        User user = new User();
        user.setName(requestDTO.getName());
        user.setNickname(requestDTO.getNickname());
        user.setEmail(requestDTO.getEmail());
        user.setPassword(hashedPassword);
        user.setRoles(new HashSet<>(Collections.singletonList(userRole)));

        userRepository.save(user);
        skillRepository.save(createDefaultSkill(user));
        return convertToDTO(user);
    }

    @Override
    public UserResponseDTO updateUserById(Long id, UserUpdateDTO requestDTO) {
        if (!userRepository.existsById(id)) {
            return null;
        }

        User user = userRepository.findById(id).orElse(null);

        if (userRepository.existsByEmailOrNickname(requestDTO.getEmail(), requestDTO.getNickname())) {
            return null;
        }
        
        user.setName(requestDTO.getName());
        user.setNickname(requestDTO.getNickname());
        user.setEmail(requestDTO.getEmail());

        userRepository.save(user);
        return convertToDTO(user);
    }

    @Override
    public boolean changePasswordById(User authenticatedUser, Long id, ChangePasswordDTO requestDTO) {
        if (!userRepository.existsById(id)) {
            return false;
        }

        User user = userRepository.findById(id).orElse(null);
        if (user.getUsername().equals(authenticatedUser.getUsername())) {
            if (!passwordEncoder.matches(requestDTO.getCurrentPassword(), user.getPassword())) {
                return false;
            }
            if (requestDTO.getCurrentPassword().equals(requestDTO.getNewPassword())) {
                return false;
            }
    
        } else if (authenticatedUser.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return false;
        }
        
        String newPassword = requestDTO.getNewPassword();
        user.setPassword(passwordEncoder.encode(newPassword));

        userRepository.save(user);
        return true;
    }

    @Override
    public boolean deleteUserById(Long id) {
        if (!userRepository.existsById(id)) {
            return false;
        }

        User user = userRepository.findById(id).orElse(null);
        userRepository.delete(user);
        return true;
    }
    
}

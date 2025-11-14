package com.fatec.runestudy.service.impl;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.fatec.runestudy.domain.dto.request.ChangePasswordRequest;
import com.fatec.runestudy.domain.dto.request.UserCreateRequest;
import com.fatec.runestudy.domain.dto.request.UserUpdateRequest;
import com.fatec.runestudy.domain.dto.response.UserResponse;
import com.fatec.runestudy.domain.model.Avatar;
import com.fatec.runestudy.domain.model.Role;
import com.fatec.runestudy.domain.model.Skill;
import com.fatec.runestudy.domain.model.User;
import com.fatec.runestudy.domain.repository.AvatarRepository;
import com.fatec.runestudy.domain.repository.RoleRepository;
import com.fatec.runestudy.domain.repository.SkillRepository;
import com.fatec.runestudy.domain.repository.UserRepository;
import com.fatec.runestudy.exception.DuplicateResourceException;
import com.fatec.runestudy.exception.ResourceNotFoundException;
import com.fatec.runestudy.exception.SamePasswordException;
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
    private AvatarRepository avatarRepository;

    @Autowired
    private SkillRepository skillRepository;

    @Override
    public UserResponse convertToDTO(User user) {
        return new UserResponse(
            user.getId(),
            user.getName(),
            user.getNickname(),
            user.getEmail(),
            user.getCurrentAvatar().getIcon(),
            user.getLevel(),
            user.getCreatedAt(),
            user.getTotalXP(),
            user.getTotalCoins());
    }

    @Override
    public Skill createDefaultSkill(User user) {
        Skill skill = new Skill();
        skill.setName("Habilidade Inicial");
        skill.setIcon("📝");
        skill.setUser(user);
        return skill;
    }

    @Override
    public UserResponse getById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Erro: Usuário não encontrado."));

        return convertToDTO(user);
    }

    @Override
    public List<UserResponse> getAll() {
        List<User> users = userRepository.findAll();

        if (users.isEmpty()) {
            throw new ResourceNotFoundException("Erro: Nenhum usuário encontrado.");
        }

        return users.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    @Override
    public UserResponse createUser(UserCreateRequest request) {
        if (userRepository.existsByEmailOrNickname(request.getEmail(), request.getNickname())) {
            throw new DuplicateResourceException("Erro: Email/Nickname já existentes.");
        }

        String hashedPassword = passwordEncoder.encode(request.getPassword());
        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new ResourceNotFoundException("Erro: Role padrão não encontrado."));
        Avatar initialAvatar = avatarRepository.findByName("Pessoa")
                .orElseThrow(() -> new ResourceNotFoundException("Erro: Avatar inicial não encontrado."));

        User user = new User();
        user.setName(request.getName());
        user.setNickname(request.getNickname());
        user.setEmail(request.getEmail());
        user.setCurrentAvatar(initialAvatar);
        user.setPassword(hashedPassword);
        user.setRoles(new HashSet<>(Collections.singletonList(userRole)));
        user.setOwnedAvatars(new HashSet<>());

        userRepository.save(user);
        skillRepository.save(createDefaultSkill(user));
        return convertToDTO(user);
    }

    @Override
    public UserResponse updateUserById(Long id, UserUpdateRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Erro: Usuário não encontrado."));

        if (userRepository.existsByEmailOrNickname(request.getEmail(), request.getNickname())) {
            throw new DuplicateResourceException("Erro: Email/Nickname já existentes.");
        }
        
        user.setName(request.getName());
        user.setNickname(request.getNickname());
        user.setEmail(request.getEmail());

        userRepository.save(user);
        return convertToDTO(user);
    }

    @Override
    public void changePassword(Long id, ChangePasswordRequest requestDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Erro: Usuário não encontrado."));
        
        if (passwordEncoder.matches(requestDTO.getNewPassword(), user.getPassword())) {
            throw new SamePasswordException();
        }
        
        String newPassword = requestDTO.getNewPassword();
        user.setPassword(passwordEncoder.encode(newPassword));

        userRepository.save(user);
    }

    @Override
    public void deleteUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Erro: Usuário não encontrado."));

        userRepository.delete(user);
    }
    
}

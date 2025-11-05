package com.fatec.runestudy.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fatec.runestudy.domain.dto.SkillRequestDTO;
import com.fatec.runestudy.domain.dto.SkillResponseDTO;
import com.fatec.runestudy.domain.model.Skill;
import com.fatec.runestudy.domain.model.User;
import com.fatec.runestudy.domain.repository.SkillRepository;
import com.fatec.runestudy.service.SkillService;

@Service
public class SkillServiceImpl implements SkillService {

    @Autowired
    private SkillRepository skillRepository;

    @Override
    public SkillResponseDTO convertToDTO(Skill skill) {
        return new SkillResponseDTO(
            skill.getName(),
            skill.getPoints(),
            skill.getDifficult(),
            skill.getTotalXP());
    }

    @Override
    public boolean isOwner(Long skillId, Long userId) {
        if (!skillRepository.existsById(skillId)) {
            return false;
        }

        Skill skill = skillRepository.findById(skillId).orElse(null);
        return skill.getUser().getId().equals(userId);
    }

    @Override
    public SkillResponseDTO getById(Long id) {
        Skill skill = skillRepository.findById(id).orElse(null);

        if (skill == null) {
            return null;
        }

        return convertToDTO(skill);
    }

    @Override
    public List<SkillResponseDTO> getAll() {
        List<Skill> skills = skillRepository.findAll();

        if (skills.isEmpty()) {
            return new ArrayList<>();
        }

        return skills.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    @Override
    public List<SkillResponseDTO> getByUserId(Long id) {
        List<Skill> skills = skillRepository.findByUserId(id);

        if (skills.isEmpty()) {
            return new ArrayList<>();
        }

        return skills.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    @Override
    public SkillResponseDTO createSkill(SkillRequestDTO requestDTO, User user) {
        if (skillRepository.existsByName(requestDTO.getName())) {
            return null;
        }

        Skill skill = new Skill();
        skill.setName(requestDTO.getName());
        skill.setDifficult(requestDTO.getDifficult());
        skill.setUser(user);

        skillRepository.save(skill);
        return convertToDTO(skill);
    }

    @Override
    public SkillResponseDTO updateSkillById(Long id, SkillRequestDTO requestDTO) {
        if (!skillRepository.existsByName(requestDTO.getName())) {
            return null;
        }

        Skill skill = skillRepository.findById(id).orElse(null);
        skill.setName(requestDTO.getName());
        skill.setDifficult(requestDTO.getDifficult());

        skillRepository.save(skill);
        return convertToDTO(skill);
    }

    @Override
    public boolean deleteSkillById(Long id) {
        if (!skillRepository.existsById(id)) {
            return false;
        }

        Skill skill = skillRepository.findById(id).orElse(null);
        skillRepository.delete(skill);
        return true;
    }
    
}

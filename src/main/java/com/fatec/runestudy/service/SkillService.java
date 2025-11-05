package com.fatec.runestudy.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.fatec.runestudy.domain.dto.SkillRequestDTO;
import com.fatec.runestudy.domain.dto.SkillResponseDTO;
import com.fatec.runestudy.domain.model.Skill;
import com.fatec.runestudy.domain.model.User;

@Service
public interface SkillService {
    
    SkillResponseDTO convertToDTO(Skill skill);

    boolean isOwner(Long skillId, Long userId);

    SkillResponseDTO getById(Long id);

    List<SkillResponseDTO> getAll();

    List<SkillResponseDTO> getByUserId(Long id);

    SkillResponseDTO createSkill(SkillRequestDTO requestDTO, User user);

    SkillResponseDTO updateSkillById(Long id, SkillRequestDTO requestDTO);

    boolean deleteSkillById(Long id);
    
}

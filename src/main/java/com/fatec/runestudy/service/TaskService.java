package com.fatec.runestudy.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.fatec.runestudy.domain.dto.ChangeSkillDTO;
import com.fatec.runestudy.domain.dto.TaskRequestDTO;
import com.fatec.runestudy.domain.dto.TaskResponseDTO;
import com.fatec.runestudy.domain.model.Skill;
import com.fatec.runestudy.domain.model.Task;
import com.fatec.runestudy.domain.model.User;

@Service
public interface TaskService {
    
    TaskResponseDTO convertToDTO(Task task);

    boolean isOwner(Long taskId, Long userId);

    boolean isFromSkill(Long taskId, Long skillId);

    TaskResponseDTO getById(Long id);

    List<TaskResponseDTO> getAll();

    List<TaskResponseDTO> getByUserId(Long id);

    List<TaskResponseDTO> getBySkillId(Long id);

    TaskResponseDTO createTask(TaskRequestDTO requestDTO, User user, Skill skill);

    TaskResponseDTO updateTaskById(Long id, TaskRequestDTO requestDTO);

    boolean changeSkillById(Long taskId, ChangeSkillDTO requestDTO);

    boolean deleteTaskById(Long id);

}

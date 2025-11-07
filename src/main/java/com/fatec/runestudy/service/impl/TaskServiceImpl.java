package com.fatec.runestudy.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fatec.runestudy.domain.dto.ChangeSkillDTO;
import com.fatec.runestudy.domain.dto.TaskRequestDTO;
import com.fatec.runestudy.domain.dto.TaskResponseDTO;
import com.fatec.runestudy.domain.model.Skill;
import com.fatec.runestudy.domain.model.Task;
import com.fatec.runestudy.domain.model.User;
import com.fatec.runestudy.domain.repository.SkillRepository;
import com.fatec.runestudy.domain.repository.TaskRepository;
import com.fatec.runestudy.service.TaskService;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private SkillRepository skillRepository;

    @Override
    public TaskResponseDTO convertToDTO(Task task) {
        return new TaskResponseDTO(
            task.getTitle(),
            task.getDescription(),
            task.isBlock(),
            task.getStatus(),
            task.getTaskXP());
    }

    @Override
    public boolean isOwner(Long taskId, Long userId) {
        if (!taskRepository.existsById(taskId)) {
            return false;
        }

        Task task = taskRepository.findById(taskId).orElse(null);
        return task.getUser().getId().equals(userId);
    }

    @Override
    public boolean isFromSkill(Long taskId, Long skillId) {
        if (!taskRepository.existsById(taskId)) {
            return false;
        }

        Task task = taskRepository.findById(taskId).orElse(null);
        return task.getSkill().getId().equals(skillId);
    }

    @Override
    public TaskResponseDTO getById(Long id) {
        Task task = taskRepository.findById(id).orElse(null);

        if (task == null) {
            return null;
        }

        return convertToDTO(task);
    }

    @Override
    public List<TaskResponseDTO> getAll() {
        List<Task> tasks = taskRepository.findAll();

        if (tasks.isEmpty()) {
            return new ArrayList<>();
        }

        return tasks.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    @Override
    public List<TaskResponseDTO> getByUserId(Long id) {
        List<Task> tasks = taskRepository.findByUserId(id);

        if (tasks.isEmpty()) {
            return new ArrayList<>();
        }

        return tasks.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    @Override
    public List<TaskResponseDTO> getBySkillId(Long id) {
        List<Task> tasks = taskRepository.findBySkillId(id);

        if (tasks.isEmpty()) {
            return new ArrayList<>();
        }

        return tasks.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    @Override
    public TaskResponseDTO createTask(TaskRequestDTO requestDTO, User user, Long skillId) {
        if (taskRepository.existsByTitle(requestDTO.getTitle()) || !skillRepository.existsById(skillId)) {
            return null;
        }

        Skill skill = skillRepository.findById(skillId).orElse(null);
        
        Task task = new Task();
        task.setTitle(requestDTO.getTitle());
        task.setDescription(requestDTO.getDescription());
        task.setTaskXP(requestDTO.getTaskXP());
        task.setUser(user);
        task.setSkill(skill);

        taskRepository.save(task);
        return convertToDTO(task);
    }

    @Override
    public TaskResponseDTO updateTaskById(Long id, TaskRequestDTO requestDTO) {
        if (!taskRepository.existsByTitle(requestDTO.getTitle())) {
            return null;
        }

        Task task = taskRepository.findById(id).orElse(null);
        if (task.isBlock()) {
            return null;
        }

        task.setTitle(requestDTO.getTitle());
        task.setDescription(requestDTO.getDescription());
        task.setTaskXP(requestDTO.getTaskXP());

        taskRepository.save(task);
        return convertToDTO(task);
    }

    @Override
    public boolean changeSkillById(Long taskId, ChangeSkillDTO requestDTO) {
        if (!taskRepository.existsById(taskId)) {
            return false;
        }
        if (!skillRepository.existsById(requestDTO.getSkillId())) {
            return false;
        }

        Task task = taskRepository.findById(taskId).orElse(null);
        if (task.isBlock()) {
            return false;
        }

        Skill skill = skillRepository.findById(requestDTO.getSkillId()).orElse(null);
        task.setSkill(skill);

        taskRepository.save(task);
        return true;
    }

    @Override
    public boolean deleteTaskById(Long id) {
        if (!taskRepository.existsById(id)) {
            return false;
        }

        Task task = taskRepository.findById(id).orElse(null);
        taskRepository.delete(task);
        return true;
    }
    
}

package com.fatec.runestudy.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fatec.runestudy.domain.dto.request.TaskRequest;
import com.fatec.runestudy.domain.dto.response.TaskResponse;
import com.fatec.runestudy.domain.model.Skill;
import com.fatec.runestudy.domain.model.Task;
import com.fatec.runestudy.domain.model.User;
import com.fatec.runestudy.domain.repository.SkillRepository;
import com.fatec.runestudy.domain.repository.TaskRepository;
import com.fatec.runestudy.exception.BlockedTaskException;
import com.fatec.runestudy.exception.ResourceNotFoundException;
import com.fatec.runestudy.service.TaskService;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private SkillRepository skillRepository;

    @Override
    public TaskResponse convertToDTO(Task task) {
        boolean block = task.getStatus() == "blocked";

        return new TaskResponse(
            task.getId(),
            task.getTitle(),
            task.getDescription(),
            task.getStatus(),
            block,
            task.getTaskXP());
    }

    @Override
    public boolean isOwner(Long taskId, Long userId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Erro: Tarefa não encontrada"));

        return task.getUser().getId().equals(userId);
    }

    @Override
    public boolean isFromSkill(Long taskId, Long skillId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Erro: Tarefa não encontrada"));
        
        return task.getSkill().getId().equals(skillId);
    }

    @Override
    public TaskResponse getById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Erro: Tarefa não encontrada"));

        return convertToDTO(task);
    }

    @Override
    public List<TaskResponse> getAll() {
        List<Task> tasks = taskRepository.findAll();

        if (tasks.isEmpty()) {
            throw new ResourceNotFoundException("Erro: Nenhuma tarefa encontrada.");
        }

        return tasks.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    @Override
    public List<TaskResponse> getByUserId(Long id) {
        List<Task> tasks = taskRepository.findByUserId(id);

        if (tasks.isEmpty()) {
            throw new ResourceNotFoundException("Erro: Nenhuma tarefa encontrada.");
        }

        return tasks.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    @Override
    public List<TaskResponse> getBySkillId(Long id) {
        List<Task> tasks = taskRepository.findBySkillId(id);

        if (tasks.isEmpty()) {
            throw new ResourceNotFoundException("Erro: Nenhuma tarefa encontrada.");
        }

        return tasks.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    @Override
    public TaskResponse createTask(TaskRequest request, User user) {
        Skill skill = skillRepository.findByNameAndUser(request.getSkillName(), user)
                .orElseThrow(() -> new ResourceNotFoundException("Erro: Habilidade não encontrada."));        
        
        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setTaskXP(request.getTaskXP());
        task.setUser(user);
        task.setSkill(skill);

        taskRepository.save(task);
        return convertToDTO(task);
    }

    @Override
    public TaskResponse updateTaskById(Long id, TaskRequest request) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Erro: Tarefa não encontrada"));
        
        if (task.getStatus().equals("blocked")) {
            throw new BlockedTaskException();
        }

        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setTaskXP(request.getTaskXP());

        taskRepository.save(task);
        return convertToDTO(task);
    }

    @Override
    public TaskResponse toggleTaskBlock(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Erro: Tarefa não encontrada"));
        
        boolean block = task.getStatus().equals("blocked");
        task.setStatus(block ? "pending" : "blocked");

        taskRepository.save(task);
        return convertToDTO(task);
    }

    @Override
    public void deleteTaskById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Erro: Tarefa não encontrada"));
        
        if (task.getStatus().equals("blocked")) {
            throw new BlockedTaskException();
        }

        taskRepository.delete(task);
    }
    
}

package com.fatec.runestudy.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.fatec.runestudy.domain.dto.request.TaskCreateRequest;
import com.fatec.runestudy.domain.dto.request.TaskUpdateRequest;
import com.fatec.runestudy.domain.dto.response.TaskResponse;
import com.fatec.runestudy.domain.model.Task;
import com.fatec.runestudy.domain.model.User;

@Service
public interface TaskService {
    
    TaskResponse convertToDTO(Task task);

    boolean isOwner(Long taskId, Long userId);

    boolean isFromSkill(Long taskId, Long skillId);

    TaskResponse getById(Long id);

    List<TaskResponse> getAll();

    List<TaskResponse> getByUserId(Long userId);

    List<TaskResponse> getBySkillId(Long skillId);

    void createTask(TaskCreateRequest request, User user);

    void updateTaskById(Long id, TaskUpdateRequest request);

    void toggleTaskBlock(Long id);

    void markTaskAsComplete(Long id);

    void deleteTaskById(Long id);

}

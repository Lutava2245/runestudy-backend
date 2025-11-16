package com.fatec.runestudy.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.fatec.runestudy.domain.dto.request.TaskRequest;
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

    TaskResponse createTask(TaskRequest request, User user);

    TaskResponse updateTaskById(Long id, TaskRequest request);

    TaskResponse toggleTaskBlock(Long id);

    TaskResponse markTaskAsComplete(Long id);

    void deleteTaskById(Long id);

}

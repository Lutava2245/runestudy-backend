package com.fatec.runestudy.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fatec.runestudy.domain.dto.request.TaskRequest;
import com.fatec.runestudy.domain.dto.response.TaskResponse;
import com.fatec.runestudy.domain.model.User;
import com.fatec.runestudy.service.SkillService;
import com.fatec.runestudy.service.TaskService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("api/tasks")
public class TaskController {
    
    @Autowired
    private TaskService taskService;

    @Autowired
    private SkillService skillService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<TaskResponse>> getAllTasks() {
        List<TaskResponse> taskResponses = taskService.getAll();
        return ResponseEntity.ok(taskResponses);
    }
    
    @GetMapping("skill/{id}")
    @PreAuthorize("hasRole('ADMIN') or @skillService.isOwner(#id, principal.id)")
    public ResponseEntity<List<TaskResponse>> getAllTasksBySkill(@PathVariable Long id) {
        List<TaskResponse> taskResponses = taskService.getBySkillId(id);
        return ResponseEntity.ok(taskResponses);
    }

    @GetMapping("user/{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == principal.id")
    public ResponseEntity<List<TaskResponse>> getAllTasksByUser(@PathVariable Long id) {
        List<TaskResponse> taskResponses = taskService.getByUserId(id);
        return ResponseEntity.ok(taskResponses);
    }

    @GetMapping("{id}")
    @PreAuthorize("hasRole('ADMIN') or @taskService.isOwner(#id, principal.id)")
    public ResponseEntity<TaskResponse> getTask(@PathVariable Long id) {
        TaskResponse taskResponse = taskService.getById(id);
        return ResponseEntity.ok(taskResponse);
    }

    @PostMapping("register")
    @PreAuthorize("hasRole('ADMIN') or @skillService.isOwner(#request.getSkillId(), principal.id)")
    public ResponseEntity<TaskResponse> registerTask(@RequestBody TaskRequest request, @AuthenticationPrincipal User user) {
        TaskResponse taskResponse = taskService.createTask(request, user);
        return ResponseEntity.ok(taskResponse);
    }

    @PutMapping("{id}")
    @PreAuthorize("hasRole('ADMIN') or @taskService.isOwner(#id, principal.id)")
    public ResponseEntity<TaskResponse> editTask(@PathVariable Long id, @RequestBody TaskRequest request) {
        TaskResponse taskResponse = taskService.updateTaskById(id, request);
        return ResponseEntity.ok(taskResponse);
    }

    @PatchMapping("{id}/block")
    @PreAuthorize("hasRole('ADMIN') or @taskService.isOwner(#id, principal.id)")
    public ResponseEntity<TaskResponse> blockTask(@PathVariable Long id) {
        TaskResponse taskResponse = taskService.toggleTaskBlock(id);
        return ResponseEntity.ok(taskResponse);
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('ADMIN') or @taskService.isOwner(#id, principal.id)")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTaskById(id);
        return ResponseEntity.noContent().build();
    }

}

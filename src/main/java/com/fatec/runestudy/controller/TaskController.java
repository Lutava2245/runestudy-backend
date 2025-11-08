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

import com.fatec.runestudy.domain.dto.BlockTaskDTO;
import com.fatec.runestudy.domain.dto.ChangeSkillDTO;
import com.fatec.runestudy.domain.dto.TaskRequestDTO;
import com.fatec.runestudy.domain.dto.TaskResponseDTO;
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
    public ResponseEntity<List<TaskResponseDTO>> getAllTasks() {
        List<TaskResponseDTO> tasks = taskService.getAll();
        return tasks.isEmpty()
            ? ResponseEntity.notFound().build()
            : ResponseEntity.ok(tasks);
    }
    
    @GetMapping("skill/{id}")
    @PreAuthorize("hasRole('ADMIN') or @skillService.isOwner(#id, principal.id)")
    public ResponseEntity<List<TaskResponseDTO>> getAllTasksBySkill(@PathVariable Long id) {
        List<TaskResponseDTO> tasks = taskService.getBySkillId(id);
        return tasks.isEmpty()
            ? ResponseEntity.notFound().build()
            : ResponseEntity.ok(tasks);
    }

    @GetMapping("user/{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == principal.id")
    public ResponseEntity<List<TaskResponseDTO>> getAllTasksByUser(@PathVariable Long id) {
        List<TaskResponseDTO> tasks = taskService.getByUserId(id);
        return tasks.isEmpty()
            ? ResponseEntity.notFound().build()
            : ResponseEntity.ok(tasks);
    }

    @GetMapping("{id}")
    @PreAuthorize("hasRole('ADMIN') or @taskService.isOwner(#id, principal.id)")
    public ResponseEntity<TaskResponseDTO> getTask(@PathVariable Long id) {
        TaskResponseDTO task = taskService.getById(id);
        return task != null
            ? ResponseEntity.ok(task)
            : ResponseEntity.notFound().build();
    }

    @PostMapping("register")
    @PreAuthorize("hasRole('ADMIN') or @skillService.isOwner(#requestDTO.getSkillId(), principal.id)")
    public ResponseEntity<TaskResponseDTO> registerTask(@RequestBody TaskRequestDTO requestDTO, @AuthenticationPrincipal User user) {
        TaskResponseDTO task = taskService.createTask(requestDTO, user);
        return task != null
            ? ResponseEntity.ok(task)
            : ResponseEntity.badRequest().build();
    }

    @PutMapping("{id}")
    @PreAuthorize("hasRole('ADMIN') or @taskService.isOwner(#id, principal.id)")
    public ResponseEntity<TaskResponseDTO> editTask(@PathVariable Long id, @RequestBody TaskRequestDTO requestDTO) {
        TaskResponseDTO task = taskService.updateTaskById(id, requestDTO);
        return task != null
            ? ResponseEntity.ok(task)
            : ResponseEntity.badRequest().build();
    }

    @PatchMapping("{id}/skill")
    @PreAuthorize("hasRole('ADMIN') or @taskService.isOwner(#id, principal.id) and @skillService.isOwner(@requestDTO.getSkillId(), principal.id)")
    public ResponseEntity<Void> changeSkill(@PathVariable Long id, @RequestBody ChangeSkillDTO requestDTO) {
        return taskService.changeSkillById(id, requestDTO)
            ? ResponseEntity.noContent().build()
            : ResponseEntity.badRequest().build();
    }

    @PatchMapping("{id}/block")
    @PreAuthorize("hasRole('ADMIN') or @taskService.isOwner(#id, principal.id)")
    public ResponseEntity<Void> blockTask(@PathVariable Long id, @RequestBody BlockTaskDTO requestDTO) {
        return taskService.blockTaskById(id, requestDTO)
            ? ResponseEntity.noContent().build()
            : ResponseEntity.badRequest().build();
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('ADMIN') or @taskService.isOwner(#id, principal.id)")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        return taskService.deleteTaskById(id)
            ? ResponseEntity.noContent().build()
            : ResponseEntity.notFound().build();
    }

}

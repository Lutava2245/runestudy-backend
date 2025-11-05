package com.fatec.runestudy.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fatec.runestudy.domain.dto.SkillRequestDTO;
import com.fatec.runestudy.domain.dto.SkillResponseDTO;
import com.fatec.runestudy.domain.model.User;
import com.fatec.runestudy.service.SkillService;

@RestController
@RequestMapping("api/skills")
public class SkillController {
    
    @Autowired
    private SkillService skillService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<SkillResponseDTO>> getAllSkills() {
        List<SkillResponseDTO> skills = skillService.getAll();
        return skills.isEmpty()
            ? ResponseEntity.notFound().build()
            : ResponseEntity.ok(skills);
    }

    @GetMapping("user/{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == principal.id")
    public ResponseEntity<List<SkillResponseDTO>> getAllSkillsByUser(@PathVariable Long id) {
        List<SkillResponseDTO> skills = skillService.getByUserId(id);
        return skills.isEmpty()
            ? ResponseEntity.notFound().build()
            : ResponseEntity.ok(skills);
    }

    @GetMapping("{id}")
    @PreAuthorize("hasRole('ADMIN') or @skillService.isOwner(#id, principal.id)")
    public ResponseEntity<SkillResponseDTO> getSkill(@PathVariable Long id) {
        SkillResponseDTO skill = skillService.getById(id);
        return skill != null
            ? ResponseEntity.ok(skill)
            : ResponseEntity.notFound().build();
    }
    
    @PostMapping("register")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<SkillResponseDTO> registerSkill(@RequestBody SkillRequestDTO requestDTO, @AuthenticationPrincipal User user) {
        SkillResponseDTO skill = skillService.createSkill(requestDTO, user);
        return skill != null
            ? ResponseEntity.ok(skill)
            : ResponseEntity.badRequest().build();
    }

    @PutMapping("{id}")
    @PreAuthorize("hasRole('ADMIN') or @skillService.isOwner(#id, principal.id)")
    public ResponseEntity<SkillResponseDTO> editSkill(@RequestBody SkillRequestDTO requestDTO, @PathVariable Long id) {
        SkillResponseDTO skill = skillService.updateSkillById(id, requestDTO);
        return skill != null
            ? ResponseEntity.ok(skill)
            : ResponseEntity.badRequest().build();
    }
    
    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('ADMIN') or @skillService.isOwner(#id, principal.id)")
    public ResponseEntity<Void> deleteSkill(@PathVariable Long id) {
        return skillService.deleteSkillById(id)
            ? ResponseEntity.noContent().build()
            : ResponseEntity.notFound().build();
    }

}

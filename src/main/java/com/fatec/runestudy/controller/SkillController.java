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

import com.fatec.runestudy.domain.dto.request.SkillRequest;
import com.fatec.runestudy.domain.dto.response.SkillResponse;
import com.fatec.runestudy.domain.model.User;
import com.fatec.runestudy.service.SkillService;

@RestController
@RequestMapping("api/skills")
public class SkillController {
    
    @Autowired
    private SkillService skillService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<SkillResponse>> getAllSkills() {
        List<SkillResponse> skillResponses = skillService.getAll();
        return ResponseEntity.ok(skillResponses);
    }

    @GetMapping("user/{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == principal.id")
    public ResponseEntity<List<SkillResponse>> getAllSkillsByUser(@PathVariable Long id) {
        List<SkillResponse> skillResponses = skillService.getByUserId(id);
        return ResponseEntity.ok(skillResponses);
    }

    @GetMapping("{id}")
    @PreAuthorize("hasRole('ADMIN') or @skillService.isOwner(#id, principal.id)")
    public ResponseEntity<SkillResponse> getSkill(@PathVariable Long id) {
        SkillResponse skillResponse = skillService.getById(id);
        return skillResponse != null
            ? ResponseEntity.ok(skillResponse)
            : ResponseEntity.notFound().build();
    }
    
    @PostMapping("register")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<SkillResponse> registerSkill(@RequestBody SkillRequest requestDTO, @AuthenticationPrincipal User user) {
        SkillResponse skillResponse = skillService.createSkill(requestDTO, user);
        return ResponseEntity.ok(skillResponse);
    }

    @PutMapping("{id}")
    @PreAuthorize("hasRole('ADMIN') or @skillService.isOwner(#id, principal.id)")
    public ResponseEntity<SkillResponse> editSkill(@RequestBody SkillRequest requestDTO, @PathVariable Long id) {
        SkillResponse skillResponse = skillService.updateSkillById(id, requestDTO);
        return ResponseEntity.ok(skillResponse);
    }
    
    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('ADMIN') or @skillService.isOwner(#id, principal.id)")
    public ResponseEntity<Void> deleteSkill(@PathVariable Long id) {
        skillService.deleteSkillById(id);
        return  ResponseEntity.noContent().build();
    }

}

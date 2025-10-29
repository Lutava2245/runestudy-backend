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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fatec.runestudy.domain.dto.ChangePasswordDTO;
import com.fatec.runestudy.domain.dto.UserCreateDTO;
import com.fatec.runestudy.domain.dto.UserResponseDTO;
import com.fatec.runestudy.domain.dto.UserUpdateDTO;
import com.fatec.runestudy.domain.model.User;
import com.fatec.runestudy.service.UserService;

@RestController
@RequestMapping("api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<UserResponseDTO> users = userService.getAll();
        return users.isEmpty()
            ? ResponseEntity.notFound().build()
            : ResponseEntity.ok(users);
    }

    @GetMapping("{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == principal.id")
    public ResponseEntity<UserResponseDTO> getUser(@PathVariable Long id) {
        UserResponseDTO user = userService.getById(id);
        return user != null
            ? ResponseEntity.ok(user)
            : ResponseEntity.notFound().build();
    }
    
    @PostMapping("register")
    public ResponseEntity<UserResponseDTO> registerUser(@RequestBody UserCreateDTO requestDTO) {
        UserResponseDTO user = userService.createUser(requestDTO);
        return user != null
            ? ResponseEntity.ok(user)
            : ResponseEntity.badRequest().build();
    }
    
    @PutMapping("{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == principal.id")
    public ResponseEntity<UserResponseDTO> editUser(@RequestBody UserUpdateDTO requestDTO, @PathVariable Long id) {
        UserResponseDTO user = userService.updateUserById(id, requestDTO);
        return user != null
            ? ResponseEntity.ok(user)
            : ResponseEntity.badRequest().build();
    }

    @PatchMapping("{id}/password")
    @PreAuthorize("hasRole('ADMIN') or #id == principal.id")
    public ResponseEntity<Void> changePassword(@RequestBody ChangePasswordDTO requestDTO, @PathVariable Long id, @AuthenticationPrincipal User user) {
        return userService.changePasswordById(user, id, requestDTO)
            ? ResponseEntity.noContent().build()
            : ResponseEntity.badRequest().build();
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        return userService.deleteUserById(id)
            ? ResponseEntity.noContent().build()
            : ResponseEntity.notFound().build();
    }

}

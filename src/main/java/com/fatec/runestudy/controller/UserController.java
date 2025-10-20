package com.fatec.runestudy.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fatec.runestudy.domain.dto.UserCreateDTO;
import com.fatec.runestudy.domain.dto.UserResponseDTO;
import com.fatec.runestudy.service.UserService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


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

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<UserResponseDTO> getUser(@RequestParam Long id) {
        UserResponseDTO user = userService.getById(id);
        return user != null
            ? ResponseEntity.ok(user)
            : ResponseEntity.notFound().build();
    }
    
    
    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> registerUser(@RequestBody UserCreateDTO requestDTO) {
        UserResponseDTO user = userService.createUser(requestDTO);
        return user != null
            ? ResponseEntity.ok(user)
            : ResponseEntity.badRequest().build();
    }
    
}

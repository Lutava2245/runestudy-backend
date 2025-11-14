package com.fatec.runestudy.config;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.fatec.runestudy.domain.model.Avatar;
import com.fatec.runestudy.domain.model.Role;
import com.fatec.runestudy.domain.model.Skill;
import com.fatec.runestudy.domain.model.Task;
import com.fatec.runestudy.domain.model.User;
import com.fatec.runestudy.domain.repository.*;
import com.fatec.runestudy.exception.ResourceNotFoundException;

import io.jsonwebtoken.lang.Collections;

@Configuration
public class DataLoader {
    
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AvatarRepository avatarRepository;
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    CommandLineRunner initData() {
        return args -> {
            createRoles();
            createAvatars();
            createInitialAdmin();
            createAdminSkill();
            createAdminTask();
        };
    }

    private void createRoles() {
        List<String> requiredRoles = Arrays.asList("ROLE_USER", "ROLE_ADMIN");

        for (String roleName : requiredRoles) {
            if (roleRepository.findByName(roleName).isEmpty()) {
                
                Role role = new Role();
                role.setName(roleName);
                
                roleRepository.save(role);
                System.out.println("Role criado: " + roleName);
            }
        }
    }

    private void createAvatars() {
        Map<String, String> avatarMap = new LinkedHashMap<>();
        avatarMap.put("Pessoa", "👤");
        avatarMap.put("Mago Sábio", "🧙");
        avatarMap.put("Coroa Real", "👑");
        avatarMap.put("Cavaleiro", "⚔️");
        avatarMap.put("Escudeiro", "🛡️");
        avatarMap.put("Arqueiro", "🏹");
        avatarMap.put("Espadachim", "🗡️");
        avatarMap.put("Místico", "🔮");
        avatarMap.put("Domador de Leões", "🦁");
        avatarMap.put("Trovão", "⚡");
        avatarMap.put("Estelar", "🌟");
        avatarMap.put("Caçador de Dragões", "🐉");
        
        avatarMap.forEach((name, emoji) -> {
            if (!avatarRepository.existsByName(name)) {
                int price = switch (emoji) {
                    case "👤" -> 0;
                    case "🧙", "👑" -> 100;
                    case "⚔️", "🛡️" -> 150;
                    case "🏹", "🗡️" -> 200;
                    case "🔮", "🦁" -> 250;
                    case "⚡" -> 300;
                    case "🌟" -> 350;
                    case "🐉" -> 500;
                    default -> 0;
                };
                
                Avatar avatar = new Avatar();
                avatar.setName(name);
                avatar.setIcon(emoji);
                avatar.setPrice(price);
                avatarRepository.save(avatar);
            }
        });
    }

    private void createInitialAdmin() {
        final String ADMIN_EMAIL = "admin@runestudy.com";

        if (!userRepository.existsByEmail(ADMIN_EMAIL)) {

            // Senha inicial temporária
            String initialPassword = "GigaPowerMasterSuperMegaBlaster123456*";

            Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                    .orElseThrow(() -> new ResourceNotFoundException("Erro: ROLE_ADMIN não encontrado."));
            
            Role userRole = roleRepository.findByName("ROLE_USER")
                    .orElseThrow(() -> new ResourceNotFoundException("Erro: ROLE_USER não encontrado."));

            Avatar adminAvatar = new Avatar();
            adminAvatar.setName("ADM");
            adminAvatar.setIcon("🧑‍💻");

            List<Avatar> adminAvatars = avatarRepository.findAll();

            User admin = new User();
            admin.setEmail(ADMIN_EMAIL);
            admin.setNickname("AdminRuneStudy"); 
            admin.setName("Administrador Inicial");
            admin.setPassword(passwordEncoder.encode(initialPassword));
            admin.setCurrentAvatar(adminAvatar);
            admin.setOwnedAvatars(new HashSet<>(Collections.asSet(adminAvatars)));

            Set<Role> roles = new HashSet<>(Arrays.asList(adminRole, userRole));
            admin.setRoles(roles);

            userRepository.save(admin);
            System.out.println("Usuário Admin inicial criado com sucesso: " + ADMIN_EMAIL);
        }
    }

    private void createAdminSkill() {
        User adminUser = userRepository.findByEmail("admin@runestudy.com")
                .orElseThrow(() -> new ResourceNotFoundException("Erro: ADM não encontrado."));

        if (!skillRepository.existsByUser(adminUser)) {     
            final String SKILL_NAME = "Habilidade do ADM";
            
            Skill skill = new Skill();
            skill.setName(SKILL_NAME);
            skill.setIcon("🧑‍💻");
            skill.setUser(adminUser);

            skillRepository.save(skill);
            System.out.println("Habilidade de Admin inicial criada com sucesso: " + SKILL_NAME);
        }
    }

    private void createAdminTask() {
        User adminUser = userRepository.findByEmail("admin@runestudy.com")
                .orElseThrow(() -> new ResourceNotFoundException("Erro: ADM não encontrado."));
        
        List<Skill> adminSkills = skillRepository.findByUserId(adminUser.getId());

        if (adminSkills.isEmpty()) {
            throw new ResourceNotFoundException("Erro: Nenhuma habilidade de Admin inicial encontrada.");
        }

        Skill adminSkill = adminSkills.getFirst();

        if (!taskRepository.existsBySkill(adminSkill)) {
            final String TASK_TITLE = "Tarefa do ADM";

            Task task = new Task();
            task.setTitle(TASK_TITLE);
            task.setDescription("Descrição de template para tarefas criadas");
            task.setTaskXP(50);
            task.setUser(adminUser);
            task.setSkill(adminSkill);

            taskRepository.save(task);
            System.out.println("Tarefa de Admin inicial criada com sucesso: " + TASK_TITLE);
        }
    }

}

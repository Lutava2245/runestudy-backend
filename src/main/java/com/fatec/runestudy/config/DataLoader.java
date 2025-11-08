package com.fatec.runestudy.config;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.fatec.runestudy.domain.model.Role;
import com.fatec.runestudy.domain.model.Skill;
import com.fatec.runestudy.domain.model.Task;
import com.fatec.runestudy.domain.model.User;
import com.fatec.runestudy.domain.repository.*;

@Configuration
public class DataLoader {
    
    @Autowired
    private RoleRepository roleRepository;

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

    private void createInitialAdmin() {
        final String ADMIN_EMAIL = "admin@runestudy.com";

        if (!userRepository.existsByEmail(ADMIN_EMAIL)) {

            // Senha inicial temporária
            String initialPassword = "GigaPowerMasterSuperMegaBlaster123456*";

            Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                    .orElseThrow(() -> new RuntimeException("Erro: ROLE_ADMIN não encontrado."));
            
            Role userRole = roleRepository.findByName("ROLE_USER")
                    .orElseThrow(() -> new RuntimeException("Erro: ROLE_USER não encontrado."));

            User admin = new User();
            admin.setEmail(ADMIN_EMAIL);
            admin.setNickname("AdminRuneStudy"); 
            admin.setName("Administrador Inicial");
            admin.setPassword(passwordEncoder.encode(initialPassword));

            Set<Role> roles = new HashSet<>(Arrays.asList(adminRole, userRole));
            admin.setRoles(roles);

            userRepository.save(admin);
            System.out.println("Usuário Admin inicial criado com sucesso: " + ADMIN_EMAIL);
        }
    }

    private void createAdminSkill() {
        User adminUser = userRepository.findByEmail("admin@runestudy.com").orElse(null);

        if (!skillRepository.existsByUser(adminUser)) {     
            final String SKILL_NAME = "Habilidade Padrão";
            
            Skill skill = new Skill();
            skill.setName(SKILL_NAME);
            skill.setDifficult(1);
            skill.setUser(adminUser);

            skillRepository.save(skill);
            System.out.println("Habilidade de Admin inicial criada com sucesso: " + SKILL_NAME);
        }
    }

    private void createAdminTask() {
        User adminUser = userRepository.findByEmail("admin@runestudy.com").orElse(null);
        Skill skill = skillRepository.findByUserId(adminUser.getId()).getFirst();

        if (!taskRepository.existsBySkill(skill)) {
            final String TASK_TITLE = "Tarefa inicial";

            Task task = new Task();
            task.setTitle(TASK_TITLE);
            task.setDescription("Descrição de template para tarefas criadas");
            task.setTaskXP(100);
            task.setUser(adminUser);
            task.setSkill(skill);

            taskRepository.save(task);
            System.out.println("Tarefa de Admin inicial criada com sucesso: " + TASK_TITLE);
        }
    }

}

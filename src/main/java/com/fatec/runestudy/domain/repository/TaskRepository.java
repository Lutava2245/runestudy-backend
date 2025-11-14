package com.fatec.runestudy.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fatec.runestudy.domain.model.Skill;
import com.fatec.runestudy.domain.model.Task;
import com.fatec.runestudy.domain.model.User;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    
    boolean existsByTitleAndUser(String title, User user);

    boolean existsBySkill(Skill skill);

    Optional<Task> findByTitle(String title);

    List<Task> findBySkillId(Long id);

    List<Task> findByUserId(Long id);

}

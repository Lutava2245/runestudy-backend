package com.fatec.runestudy.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fatec.runestudy.domain.model.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    
    boolean existsByName(String name);

    Optional<Task> findByName(String name);

    List<Task> findBySkillId(Long id);

    List<Task> findByUserId(Long id);

}

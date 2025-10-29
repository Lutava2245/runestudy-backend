package com.fatec.runestudy.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fatec.runestudy.domain.model.Skill;

@Repository
public interface SkillRepository extends JpaRepository<Skill, Long> {
    
    boolean existsByName(String name);

    Optional<Skill> findByName(String name);

    List<Skill> findByUserId(Long id);

}

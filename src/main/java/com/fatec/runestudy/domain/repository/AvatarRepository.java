package com.fatec.runestudy.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fatec.runestudy.domain.model.Avatar;

@Repository
public interface AvatarRepository extends JpaRepository<Avatar, Long> {

    boolean existsByName(String name);

    Optional<Avatar> findByName(String name);

}

package com.fatec.runestudy.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fatec.runestudy.domain.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByNickname(String nickname);

    boolean existsByEmail(String email);

    Optional<User> findByNickname(String nickname);

    Optional<User> findByEmail(String email);
    
}

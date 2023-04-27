package com.fastned.solarcharging.repository;

import com.fastned.solarcharging.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByUsernameIgnoreCase(String name);

    Optional<User> findByUsername(String username);
}

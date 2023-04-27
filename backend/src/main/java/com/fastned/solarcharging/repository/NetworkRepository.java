package com.fastned.solarcharging.repository;

import com.fastned.solarcharging.model.Network;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NetworkRepository extends JpaRepository<Network, Long> {

    boolean existsById(Long id);

    List<Network> findAllByUserId(long userId);
}

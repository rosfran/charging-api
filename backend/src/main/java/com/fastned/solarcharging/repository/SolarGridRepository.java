package com.fastned.solarcharging.repository;

import com.fastned.solarcharging.model.SolarGrid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SolarGridRepository extends JpaRepository<SolarGrid, Long> {

    boolean existsByNameIgnoreCase(String name);
}

package com.fastned.solarcharging.repository;

import com.fastned.solarcharging.model.Network;
import com.fastned.solarcharging.model.SolarGrid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SolarGridRepository extends JpaRepository<SolarGrid, Long> {

    boolean existsByNameIgnoreCase(String name);

    Page<SolarGrid> findAllByNetworkId(long idUser, Pageable page);

}

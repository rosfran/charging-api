package com.fastned.solarcharging.repository;

import com.fastned.solarcharging.model.Network;
import com.fastned.solarcharging.model.State;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StateRepository extends JpaRepository<State, Long> {

    List<State> findAllBySolarGridId(long idSolarGrid);

}

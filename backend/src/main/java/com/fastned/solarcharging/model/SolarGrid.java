package com.fastned.solarcharging.model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@EqualsAndHashCode(of = {"name"})
public class SolarGrid {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "sequence-solar-grid"
    )
    @SequenceGenerator(
            name = "sequence-solar-grid",
            sequenceName = "sequence_solar_grid",
            allocationSize = 5
    )
    private Long id;

    @Column(length = 500, nullable = false, unique = true)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_network", referencedColumnName = "id", nullable = false)
    private Network network;

    @OneToMany(mappedBy = "state", cascade = CascadeType.ALL)
    private Set<State> states = new HashSet<>();

}

package com.fastned.solarcharging.model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
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

    @Column(nullable = false)
    private Integer age;

    @Column(nullable = false)
    private Double powerOutput;

    @Column(nullable = false)
    private Date createdAt;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "network_id", referencedColumnName = "id", nullable = false)
    private Network network;


}

package com.fastned.solarcharging.model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
@EqualsAndHashCode(of = {"name"})
public class State {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "sequence-state"
    )
    @SequenceGenerator(
            name = "sequence-state",
            sequenceName = "sequence_state",
            allocationSize = 5
    )
    private Long id;

    @Column(nullable = false)
    private Integer age;

    @Column(nullable = false)
    private Double powerOutput;

    @Column(nullable = false)
    private Date createdAt;

    @Column(nullable = false)
    private Boolean isFirstState;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "solar_grid_id", referencedColumnName = "id", nullable = false)
    private SolarGrid solarGrid;

}

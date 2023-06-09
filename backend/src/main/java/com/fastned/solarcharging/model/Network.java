package com.fastned.solarcharging.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@Entity
public class Network {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "sequence-network"
    )
    @SequenceGenerator(
            name = "sequence-network",
            sequenceName = "sequence_network",
            allocationSize = 5
    )
    private Long id;

    @Column(length = 50, nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "network", cascade = CascadeType.ALL)
    private Set<SolarGrid> solarGrids = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Network)) return false;
        Network network = (Network) o;
        return getId() != null &&
                Objects.equals(getId(), network.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

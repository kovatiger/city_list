package com.example.test_task.model;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.*;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "country")
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Country {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "country_id")
    private UUID id;
    @Column(name = "country_name")
    private String countryName;
    @Column(name = "logo_name")
    private String logoName;
    @OneToMany(mappedBy = "country", fetch = FetchType.LAZY)
    private List<City> cities;
}
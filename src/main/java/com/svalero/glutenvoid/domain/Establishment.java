package com.svalero.glutenvoid.domain;

import com.svalero.glutenvoid.domain.heritages.EstablishmentFavourite;
import com.svalero.glutenvoid.domain.heritages.UserFavourite;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;


@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity(name= "establishment")
public class Establishment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    @NotBlank
    private String name;

    @Column
    private String description;


    @Column
    @PositiveOrZero
    private int phoneNumber;

    @Column
    private String address;

    @Column
    private String city;


    @Column
    private double latitude;

    @Column
    private double longitude;

    @Column
    private double rating;

    @Column
    private boolean glutenFreeOption;

    @OneToMany(mappedBy = "establishment")
    private Set<EstablishmentFavourite> favouredBy;
}

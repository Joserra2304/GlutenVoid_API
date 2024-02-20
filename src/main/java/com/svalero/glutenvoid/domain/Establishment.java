package com.svalero.glutenvoid.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
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
    private String location;

    @Column
    private double latitude;

    @Column
    private double longitude;

    @Column
    private double rating;

    @Column
    @NotBlank
    private boolean glutenFreeOption;

}

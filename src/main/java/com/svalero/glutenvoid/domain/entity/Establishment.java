package com.svalero.glutenvoid.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



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
    private boolean glutenFreeOption;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = true)
    private User userId;
}

package com.svalero.glutenvoid.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    @NotBlank
    private String name;

    @Column(unique = true)
    private String barcode;

    @Column
    private String company;

    @Column
    private String description;

    @Column
    private String nutritionalInfo;

    @Column
    @NotNull
    private boolean hasGluten;

    @Column
    private String imageUrl;

    @Column
    private double rating;



}

package com.svalero.glutenvoid.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String name;

    @Column
    private String surname;

    @Column
    @NotNull
    @NotBlank
    private String email;

    @Column
    @NotNull
    @NotBlank
    private String username;

    @Column
    @NotBlank
    private String password;

    @Column
    private String profileBio;

    @Column
    @Enumerated(EnumType.STRING)
    private GlutenCondition glutenCondition;

    @Column
    private boolean isAdmin;
}



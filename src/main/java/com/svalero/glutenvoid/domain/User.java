package com.svalero.glutenvoid.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "user")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) // Evita problemas de serialización
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

    @Column(name="profile_bio")
    private String profileBio;

    @Column(name = "gluten_condition")
    @Enumerated(EnumType.STRING)
    private GlutenCondition glutenCondition;

    @Column
    private boolean admin;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Recipe> recipes;
}



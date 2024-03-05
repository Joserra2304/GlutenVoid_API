package com.svalero.glutenvoid.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecipeDto {

    private long id;
    private String name;
    private String description;
    private String ingredients;
    private String instructions;
    private int preparationTime;

    private long userId;

}

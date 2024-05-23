package com.svalero.glutenvoid.domain.dto;

import com.svalero.glutenvoid.domain.entity.Recipe;
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

    public RecipeDto(Recipe recipe) {
        this.id = recipe.getId();
        this.name = recipe.getName();
        this.description = recipe.getDescription();
        this.ingredients = recipe.getIngredients();
        this.instructions = recipe.getInstructions();
        this.preparationTime = recipe.getPreparationTime();
        if (recipe.getUser() != null) {
            this.userId = recipe.getUser().getId();
        }
    }
}
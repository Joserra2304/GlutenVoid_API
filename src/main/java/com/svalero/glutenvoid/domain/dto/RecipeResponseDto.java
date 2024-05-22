package com.svalero.glutenvoid.domain.dto;
import com.svalero.glutenvoid.domain.Recipe;

public class RecipeResponseDto {
    private long id;
    private String name;
    private String description;
    private String userName; // Nombre del usuario en lugar del objeto User completo

    // Constructor que acepta una entidad Recipe
    public RecipeResponseDto(Recipe recipe) {
        this.id = recipe.getId();
        this.name = recipe.getName();
        this.description = recipe.getDescription();
        this.userName = recipe.getUser().getName(); // Asegúrate de manejar la carga perezosa correctamente
    }

    // Getters y setters
}

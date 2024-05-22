package com.svalero.glutenvoid.domain.dto;

import com.svalero.glutenvoid.domain.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private long id;
    private String name;
    private String username;
    private String email;
    private List<RecipeDto> recipes;

    // Constructor basado en la entidad User
    public UserDto(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.recipes = user.getRecipes() != null ? user.getRecipes().stream()
                .map(RecipeDto::new)
                .collect(Collectors.toList()) : new ArrayList<>();
        System.out.println("Recetas en DTO: " + this.recipes.size());
    }
}

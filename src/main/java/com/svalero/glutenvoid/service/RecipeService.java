package com.svalero.glutenvoid.service;


import com.svalero.glutenvoid.domain.Recipe;

import java.util.List;

public interface RecipeService {

    List<Recipe> findAll();

    List<Recipe> filterByName(String name);
    List<Recipe> filterByPreparationTime(int time);

    Recipe addRecipe (Recipe recipe);

}

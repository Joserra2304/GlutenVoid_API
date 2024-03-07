package com.svalero.glutenvoid.service;


import com.svalero.glutenvoid.domain.Recipe;
import com.svalero.glutenvoid.domain.User;

import java.util.List;

public interface RecipeService {

    List<Recipe> findAll();

    Recipe findById(long id);

    List<Recipe> filterByName(String name);
    List<Recipe> filterByPreparationTime(int time);

    Recipe addRecipe (Recipe recipe);

    void deleteRecipe (long id);

}

package com.svalero.glutenvoid.service;


import com.svalero.glutenvoid.domain.GlutenCondition;
import com.svalero.glutenvoid.domain.Recipe;
import com.svalero.glutenvoid.domain.User;
import com.svalero.glutenvoid.exception.RecipeNotFoundException;

import java.util.List;
import java.util.Map;

public interface RecipeService {

    List<Recipe> findAll();

    Recipe findById(long id) throws RecipeNotFoundException;

    List<Recipe> filterByName(String name) throws RecipeNotFoundException;
    List<Recipe> filterByPreparationTime(int time) throws RecipeNotFoundException;

    Recipe addRecipe (Recipe recipe);

    void deleteRecipe (long id) throws RecipeNotFoundException;

    Recipe updateRecipeByField(long id, Map<String, Object> updates) throws RecipeNotFoundException;
}

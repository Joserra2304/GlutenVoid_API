package com.svalero.glutenvoid.service;


import com.svalero.glutenvoid.domain.dto.RecipeDto;
import com.svalero.glutenvoid.exception.RecipeNotFoundException;

import java.util.List;
import java.util.Map;

public interface RecipeService {

    List<RecipeDto> findAll();

    RecipeDto findById(long id) throws RecipeNotFoundException;

    List<RecipeDto> filterByName(String name) throws RecipeNotFoundException;
    List<RecipeDto> filterByPreparationTime(int time) throws RecipeNotFoundException;

    RecipeDto addRecipe(RecipeDto recipeDto);

    void deleteRecipe(long id) throws RecipeNotFoundException;

    RecipeDto updateRecipeByField(long id, Map<String, Object> updates) throws RecipeNotFoundException;
}
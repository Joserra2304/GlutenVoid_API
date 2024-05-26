package com.svalero.glutenvoid.service;


import com.svalero.glutenvoid.domain.dto.RecipeDto;
import com.svalero.glutenvoid.domain.entity.User;
import com.svalero.glutenvoid.exception.RecipeNotFoundException;

import java.util.List;
import java.util.Map;

public interface RecipeService {

    List<RecipeDto> findAll();

    RecipeDto findById(long id) throws RecipeNotFoundException;

    List<RecipeDto> filterByApprovedRecipe(Boolean isApproved) throws RecipeNotFoundException;
    List<RecipeDto> filterByUserId(long userId) throws RecipeNotFoundException;

    // En RecipeService.java
    RecipeDto addRecipe(RecipeDto recipeDto, User user);

    void deleteRecipe(long id) throws RecipeNotFoundException;

    RecipeDto updateRecipeByField(long id, Map<String, Object> updates) throws RecipeNotFoundException;
}
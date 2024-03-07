package com.svalero.glutenvoid.service;

import com.svalero.glutenvoid.domain.Recipe;
import com.svalero.glutenvoid.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecipeServiceImplementation implements RecipeService {

    @Autowired
    RecipeRepository recipeRepository;


    @Override
    public List<Recipe> findAll() {
        return recipeRepository.findAll();
    }

    @Override
    public Recipe findById(long id) {
        return recipeRepository.findById(id).orElseThrow();
    }

    @Override
    public List<Recipe> filterByName(String name) {
        return recipeRepository.findByName(name);
    }

    @Override
    public List<Recipe> filterByPreparationTime(int time) {
        return recipeRepository.findByPreparationTime(time);
    }

    @Override
    public Recipe addRecipe(Recipe recipe) {
        return recipeRepository.save(recipe);
    }
}

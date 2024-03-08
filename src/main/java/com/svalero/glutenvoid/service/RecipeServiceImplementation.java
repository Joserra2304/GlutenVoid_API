package com.svalero.glutenvoid.service;

import com.svalero.glutenvoid.domain.Recipe;
import com.svalero.glutenvoid.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

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

    @Override
    public void deleteRecipe(long id) {
        Recipe deleteRecipe = recipeRepository.findById(id).orElseThrow();
        recipeRepository.delete(deleteRecipe);
    }

    @Override
    public Recipe updateRecipeByField(long id, Map<String, Object> updates) {
        Recipe newUpdate = findById(id);

        updates.forEach((key, value) -> {
            switch (key) {
                case "name":
                    newUpdate.setName((String) value);
                    break;
                case "description":
                    newUpdate.setDescription((String) value);
                    break;
                case "ingredients":
                    newUpdate.setIngredients((String) value);
                    break;
                case "preparationTime":
                    newUpdate.setPreparationTime(Integer.parseInt(value.toString()));
                    break;
            }
        });

        return recipeRepository.save(newUpdate);
    }
}

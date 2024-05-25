package com.svalero.glutenvoid.service;

import com.svalero.glutenvoid.domain.entity.Recipe;
import com.svalero.glutenvoid.domain.dto.RecipeDto;
import com.svalero.glutenvoid.domain.entity.User;
import com.svalero.glutenvoid.exception.RecipeNotFoundException;
import com.svalero.glutenvoid.repository.RecipeRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RecipeServiceImplementation implements RecipeService {

    @Autowired
    RecipeRepository recipeRepository;

    @Autowired
    private ModelMapper modelMapper;


    @Override
    public List<RecipeDto> findAll() {
        List<Recipe> recipes = recipeRepository.findAll();
        return recipes.stream()
                .map(recipe -> modelMapper.map(recipe, RecipeDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public RecipeDto findById(long id) throws RecipeNotFoundException {
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new RecipeNotFoundException("Recipe not found with id: " + id));
        return modelMapper.map(recipe, RecipeDto.class);
    }

    @Override
    public List<RecipeDto> filterByApprovedRecipe(boolean isApproved) throws RecipeNotFoundException {
        List<Recipe> recipes = recipeRepository.findByApprovedRecipe(isApproved);
        return recipes.stream()
                .map(recipe -> modelMapper.map(recipe, RecipeDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<RecipeDto> filterByPreparationTime(int time) throws RecipeNotFoundException {
        List<Recipe> recipes = recipeRepository.findByPreparationTime(time);
        return recipes.stream()
                .map(recipe -> modelMapper.map(recipe, RecipeDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public RecipeDto addRecipe(RecipeDto recipeDto, User user) {
        Recipe recipe = modelMapper.map(recipeDto, Recipe.class);

        if (user.isAdmin()) {
            recipe.setApprovedRecipe(true);
        }

        Recipe savedRecipe = recipeRepository.save(recipe);
        return modelMapper.map(savedRecipe, RecipeDto.class);
    }


    @Override
    public void deleteRecipe(long id) throws RecipeNotFoundException {
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new RecipeNotFoundException("Recipe not found with id: " + id));
        recipeRepository.delete(recipe);
    }

    @Override
    public RecipeDto updateRecipeByField(long id, Map<String, Object> updates) throws RecipeNotFoundException {
        Recipe existingRecipe = recipeRepository.findById(id)
                .orElseThrow(() -> new RecipeNotFoundException("Recipe not found with id: " + id));

        updates.forEach((key, value) -> {
            switch (key) {
                case "name":
                    existingRecipe.setName((String) value);
                    break;
                case "description":
                    existingRecipe.setDescription((String) value);
                    break;
                case "ingredients":
                    existingRecipe.setIngredients((String) value);
                    break;
                case "instructions":
                    existingRecipe.setInstructions((String) value);
                    break;
                case "preparationTime":
                    existingRecipe.setPreparationTime(Integer.parseInt(value.toString()));
                    break;
                case "approvedRecipe":
                    existingRecipe.setApprovedRecipe(Boolean.parseBoolean(value.toString()));
                    break;
            }
        });

        Recipe updatedRecipe = recipeRepository.save(existingRecipe);
        return modelMapper.map(updatedRecipe, RecipeDto.class);
    }

}
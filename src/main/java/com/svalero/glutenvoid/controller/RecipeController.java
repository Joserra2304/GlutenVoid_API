package com.svalero.glutenvoid.controller;

import com.svalero.glutenvoid.domain.Recipe;
import com.svalero.glutenvoid.service.RecipeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class RecipeController {

    @Autowired
    RecipeService recipeService;

    @GetMapping ("/recipes")
    public ResponseEntity<List<Recipe>> getRecipes(
            @RequestParam(name="name", required = false, defaultValue = "") String name,
            @RequestParam(name="preparationTime", required = false, defaultValue = "") String time) {

        if (!name.isEmpty()) {
            return ResponseEntity.ok(recipeService.filterByName(name));
        } else if (!time.isEmpty()) {
            return ResponseEntity.ok(recipeService.filterByPreparationTime(Integer.parseInt(time)));
        } else {
            return ResponseEntity.ok(recipeService.findAll());
        }
    }

    @PostMapping("/recipes")
    public ResponseEntity<Recipe> addRecipe(@Valid @RequestBody Recipe recipe){
        Recipe newRecipe = recipeService.addRecipe(recipe);
        return ResponseEntity.ok(newRecipe);
    }


}

package com.svalero.glutenvoid.controller;

import com.svalero.glutenvoid.domain.Recipe;
import com.svalero.glutenvoid.domain.User;
import com.svalero.glutenvoid.domain.dto.RecipeDto;
import com.svalero.glutenvoid.service.RecipeService;
import com.svalero.glutenvoid.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class RecipeController {

    @Autowired
    RecipeService recipeService;

    @Autowired
    UserService userService;

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

    @GetMapping("/recipes/{id}")
    public ResponseEntity<Recipe> getRecipeById(@PathVariable long id){
        return ResponseEntity.ok(recipeService.findById(id));
    }

    @PostMapping("/recipes")
    public ResponseEntity<Recipe> addRecipe(@Valid @RequestBody RecipeDto recipeDto){

        User user = userService.findById(recipeDto.getUserId());
        if(user == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Recipe recipe = new Recipe();
        recipe.setName(recipeDto.getName());
        recipe.setDescription(recipeDto.getDescription());
        recipe.setInstructions(recipeDto.getInstructions());
        recipe.setIngredients(recipeDto.getIngredients());
        recipe.setPreparationTime(recipeDto.getPreparationTime());
        recipe.setUser(user);


        Recipe newRecipe = recipeService.addRecipe(recipe);
        return ResponseEntity.ok(newRecipe);
    }

    @DeleteMapping("/recipes/{id}")
    public ResponseEntity<String> deleteRecipe(@PathVariable long id){
        recipeService.deleteRecipe(id);

        String deleteMessage = "Recipe deleted successfully";
        return  ResponseEntity.ok(deleteMessage);

    }

    @PatchMapping("/recipes/{id}")
    public ResponseEntity<Recipe> updateRecipePartially(@PathVariable long id, @RequestBody Map<String, Object> updates){
        Recipe updateRecipe = recipeService.updateRecipeByField(id, updates);
        return  ResponseEntity.ok(updateRecipe);
    }


}

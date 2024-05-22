package com.svalero.glutenvoid.controller;

import com.svalero.glutenvoid.domain.Recipe;
import com.svalero.glutenvoid.domain.User;
import com.svalero.glutenvoid.domain.dto.RecipeDto;
import com.svalero.glutenvoid.domain.dto.RecipeResponseDto;
import com.svalero.glutenvoid.exception.ErrorMessage;
import com.svalero.glutenvoid.exception.RecipeNotFoundException;
import com.svalero.glutenvoid.exception.UserNotFoundException;
import com.svalero.glutenvoid.service.RecipeService;
import com.svalero.glutenvoid.service.UserService;
import jakarta.persistence.NonUniqueResultException;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class RecipeController {

    private final Logger logger = LoggerFactory.getLogger(RecipeController.class);
    @Autowired
    RecipeService recipeService;

    @Autowired
    UserService userService;

    @GetMapping ("/recipes")
    public ResponseEntity<List<Recipe>> getRecipes(
            @RequestParam(name="name", required = false, defaultValue = "") String name,
            @RequestParam(name="preparationTime", required = false, defaultValue = "") String time)
            throws RecipeNotFoundException {

        if (!name.isEmpty()) {
            return ResponseEntity.ok(recipeService.filterByName(name));
        } else if (!time.isEmpty()) {
            return ResponseEntity.ok(recipeService.filterByPreparationTime(Integer.parseInt(time)));
        } else {
            return ResponseEntity.ok(recipeService.findAll());
        }
    }

    @GetMapping("/recipes/{id}")
    public ResponseEntity<Recipe> getRecipeById(@PathVariable long id) throws RecipeNotFoundException{
        return ResponseEntity.ok(recipeService.findById(id));
    }

    @PostMapping("/recipes")
    public ResponseEntity<Object> addRecipe(@RequestBody RecipeDto recipeDto) {
        try {
            Recipe recipe = convertToEntity(recipeDto);
            Recipe savedRecipe = recipeService.addRecipe(recipe);
            RecipeDto responseDto = new RecipeDto(savedRecipe);
            return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
        } catch (UserNotFoundException | NonUniqueResultException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


    private Recipe convertToEntity(RecipeDto recipeDto) throws UserNotFoundException {
        List<User> users = userService.findByName(recipeDto.getUserName());
        if (users.isEmpty()) {
            throw new UserNotFoundException("No user found with username: " + recipeDto.getUserName());
        }
        if (users.size() > 1) {
            throw new NonUniqueResultException("More than one user found with username: " + recipeDto.getUserName());
        }
        User user = users.get(0);

        Recipe recipe = new Recipe();
        recipe.setName(recipeDto.getName());
        recipe.setDescription(recipeDto.getDescription());
        recipe.setIngredients(recipeDto.getIngredients());
        recipe.setInstructions(recipeDto.getInstructions());
        recipe.setPreparationTime(recipeDto.getPreparationTime());
        recipe.setUser(user);

        return recipe;
    }


    @DeleteMapping("/recipes/{id}")
    public ResponseEntity<String> deleteRecipe(@PathVariable long id) throws RecipeNotFoundException{
        recipeService.deleteRecipe(id);

        String deleteMessage = "Recipe deleted successfully";
        return  ResponseEntity.ok(deleteMessage);

    }

    @PatchMapping("/recipes/{id}")
    public ResponseEntity<Recipe> updateRecipePartially(
            @PathVariable long id, @RequestBody Map<String, Object> updates) throws RecipeNotFoundException{
        Recipe updateRecipe = recipeService.updateRecipeByField(id, updates);
        return  ResponseEntity.ok(updateRecipe);
    }

    @ExceptionHandler(RecipeNotFoundException.class)
    public  ResponseEntity<ErrorMessage> recipeNotFoundException(RecipeNotFoundException rnfe){
        logger.error(rnfe.getMessage(), rnfe);
        ErrorMessage notFound = new ErrorMessage(404, rnfe.getMessage());
        return new ResponseEntity<>(notFound, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessage> badRequest(MethodArgumentNotValidException manve){
        Map<String, String> errors = new HashMap<>();
        manve.getBindingResult().getAllErrors().forEach(error -> {
            String fieldname = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errors.put(fieldname, message);
        });

        logger.error(manve.getMessage(), manve);
        ErrorMessage badRequest = new ErrorMessage(400, "Bad Request", errors);
        return new ResponseEntity<>(badRequest, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessage> handleException(Exception e) {
       logger.error(e.getMessage(), e);
        ErrorMessage errorMessage = new ErrorMessage(500, "Internal Server Error");
        return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFound(UserNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(NonUniqueResultException.class)
    public ResponseEntity<String> handleNonUniqueResult(NonUniqueResultException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

}

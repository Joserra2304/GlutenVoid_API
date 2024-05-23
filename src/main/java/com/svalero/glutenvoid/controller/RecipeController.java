package com.svalero.glutenvoid.controller;

import com.svalero.glutenvoid.domain.entity.User;
import com.svalero.glutenvoid.domain.dto.RecipeDto;
import com.svalero.glutenvoid.exception.ErrorMessage;
import com.svalero.glutenvoid.exception.RecipeNotFoundException;
import com.svalero.glutenvoid.exception.UserNotFoundException;
import com.svalero.glutenvoid.service.RecipeService;
import com.svalero.glutenvoid.service.UserService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    private ModelMapper modelMapper;

    @Autowired
    UserService userService;

    @GetMapping("/recipes")
    public ResponseEntity<List<RecipeDto>> getRecipes(
            @RequestParam(name="name", required = false, defaultValue = "") String name,
            @RequestParam(name="preparationTime", required = false, defaultValue = "") String preparationTime)
            throws RecipeNotFoundException {

        List<RecipeDto> recipes;
        if (!name.isEmpty()) {
            recipes = recipeService.filterByName(name);
        } else if (!preparationTime.isEmpty()) {
            recipes = recipeService.filterByPreparationTime(Integer.parseInt(preparationTime));
        } else {
            recipes = recipeService.findAll();
        }

        return ResponseEntity.ok(recipes);
    }

    @GetMapping("/recipes/{id}")
    public ResponseEntity<RecipeDto> getRecipeById(@PathVariable long id) throws RecipeNotFoundException {
        RecipeDto recipeDto = recipeService.findById(id);
        return ResponseEntity.ok(recipeDto);
    }

    @PostMapping("/recipes")
    public ResponseEntity<RecipeDto> addRecipe(@Valid @RequestBody RecipeDto recipeDto) throws UserNotFoundException {
        // Buscar el usuario por ID desde RecipeDto
        User user = userService.findById(recipeDto.getUserId());
        if (user == null) {
            throw new UserNotFoundException("User not found with id: " + recipeDto.getUserId());
        }

        // Asignar el usuario al RecipeDto
        recipeDto.setUserId(user.getId());

        // Guardar la receta usando el servicio
        RecipeDto newRecipeDto = recipeService.addRecipe(recipeDto);
        return ResponseEntity.ok(newRecipeDto);
    }




    @DeleteMapping("/recipes/{id}")
    public ResponseEntity<String> deleteRecipe(@PathVariable long id) throws RecipeNotFoundException{
        recipeService.deleteRecipe(id);

        String deleteMessage = "Recipe deleted successfully";
        return  ResponseEntity.ok(deleteMessage);

    }

    @PatchMapping("/recipes/{id}")
    public ResponseEntity<RecipeDto> updateRecipePartially(
            @PathVariable long id, @RequestBody Map<String, Object> updates) throws RecipeNotFoundException {
        RecipeDto updatedRecipeDto = recipeService.updateRecipeByField(id, updates);
        return ResponseEntity.ok(updatedRecipeDto);
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
        ErrorMessage badRequest = new ErrorMessage(400, "Petición incorrecta", errors);
        return new ResponseEntity<>(badRequest, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessage> handleException(Exception e) {
        logger.error(e.getMessage(), e);
        ErrorMessage errorMessage = new ErrorMessage(500, "Error interno del servidor");
        return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
package com.svalero.glutenvoid.controller;

import com.svalero.glutenvoid.domain.entity.User;
import com.svalero.glutenvoid.domain.dto.RecipeDto;
import com.svalero.glutenvoid.domain.enumeration.GlutenCondition;
import com.svalero.glutenvoid.exception.ErrorMessage;
import com.svalero.glutenvoid.exception.RecipeNotFoundException;
import com.svalero.glutenvoid.exception.UserNotFoundException;
import com.svalero.glutenvoid.service.RecipeService;
import com.svalero.glutenvoid.service.UserService;
import jakarta.validation.Valid;
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
    UserService userService;

    @GetMapping("/recipes")
    public ResponseEntity<List<RecipeDto>> getRecipes(
            @RequestParam(name="name", required = false, defaultValue = "") String name,
            @RequestParam(name="preparationTime", required = false, defaultValue = "") String preparationTime)
            throws RecipeNotFoundException {

        if (!name.isEmpty()) {
            List<RecipeDto> recipes = recipeService.filterByName(name);
            logger.info("Recetas filtradas por nombre de receta");
            return ResponseEntity.status(HttpStatus.OK).body(recipes);
        } else if (!preparationTime.isEmpty()) {
            List<RecipeDto> recipes = recipeService.filterByPreparationTime(Integer.parseInt(preparationTime));
            logger.info("Recetas filtradas por tiempo de preparación");
            return ResponseEntity.status(HttpStatus.OK).body(recipes);
        } else {
            List<RecipeDto> recipes = recipeService.findAll();
            logger.info("Listado de Recetas");
            return ResponseEntity.status(HttpStatus.OK).body(recipes);
        }
    }

    @GetMapping("/recipes/{id}")
    public ResponseEntity<RecipeDto> getRecipeById(@PathVariable long id) throws RecipeNotFoundException {
        RecipeDto recipeDto = recipeService.findById(id);
        logger.info("Receta mostrada con el id: "+id);
        return ResponseEntity.status(HttpStatus.OK).body(recipeDto);
    }

    @PostMapping("/recipes")
    public ResponseEntity<RecipeDto> addRecipe(@Valid @RequestBody RecipeDto recipeDto) throws UserNotFoundException {
        User user = userService.findById(recipeDto.getUserId());
        if (user == null) {
            logger.info("Usuario no encontrado con ID: " + recipeDto.getUserId());
            throw new UserNotFoundException("User not found with id: " + recipeDto.getUserId());
        }
        RecipeDto newRecipeDto = recipeService.addRecipe(recipeDto);
        logger.info(recipeDto.getName() + " ha sido registrada");
        return ResponseEntity.status(HttpStatus.CREATED).body(newRecipeDto);
    }

    @DeleteMapping("/recipes/{id}")
    public ResponseEntity<String> deleteRecipe(@PathVariable long id) throws RecipeNotFoundException{
        recipeService.deleteRecipe(id);

        String deleteMessage = "Recipe deleted successfully";

        logger.info("Receta borrada exitosamente");
        return  ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }

    @PatchMapping("/recipes/{id}")
    public ResponseEntity<RecipeDto> updateRecipePartially(
            @PathVariable long id, @RequestBody Map<String, Object> updates) throws RecipeNotFoundException {
        RecipeDto updatedRecipeDto = recipeService.updateRecipeByField(id, updates);
        logger.info("Datos de "+updatedRecipeDto.getName()+" actualizados");
        return ResponseEntity.status(HttpStatus.OK).body(updatedRecipeDto);
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

}
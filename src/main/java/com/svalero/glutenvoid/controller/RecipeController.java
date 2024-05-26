package com.svalero.glutenvoid.controller;

import com.svalero.glutenvoid.domain.entity.User;
import com.svalero.glutenvoid.domain.dto.RecipeDto;
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
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
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
            @RequestParam(name="approved", required = false, defaultValue = "") String isApproved,
            @RequestParam(name="userId", required = false, defaultValue = "") Long userId)
            throws RecipeNotFoundException {

        if (!isApproved.isEmpty()) {
            List<RecipeDto> recipes = recipeService.filterByApprovedRecipe(Boolean.parseBoolean(isApproved));
            logger.info("Recetas filtradas por nombre de receta");
            return ResponseEntity.status(HttpStatus.OK).body(recipes);
        } else if (userId != null) {
            List<RecipeDto> recipes = recipeService.filterByUserId(userId);
            logger.info("Recetas filtradas por ID de usuario");
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
    public ResponseEntity<RecipeDto> addRecipe(@Valid @RequestBody RecipeDto recipeDto)
            throws UserNotFoundException {

        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User not found"));

        recipeDto.setUserId(user.getId());
        recipeDto.setApprovedRecipe(user.isAdmin());

        // Añadir la receta a través del servicio de recetas
        RecipeDto createdRecipe = recipeService.addRecipe(recipeDto, user);
        if (user.isAdmin()) {
            logger.info("Receta '" + createdRecipe.getName() + "', con ID: " + createdRecipe.getId()
                    + ", ha sido registrada y aprobada automáticamente.");
        } else {
            logger.info("Receta '" + createdRecipe.getName() + "', con ID: " + createdRecipe.getId()
                    + ", ha sido registrada y está pendiente de aprobación.");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(createdRecipe);
    }

    @DeleteMapping("/recipes/{id}")
    public ResponseEntity<String> deleteRecipe(@PathVariable long id) throws RecipeNotFoundException, UserNotFoundException {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User not found"));
        RecipeDto recipe = recipeService.findById(id);

        if (recipe.getUserId() != user.getId() && !user.isAdmin()) {
            logger.info("Usuario no autorizado para borrar esta receta");
            throw new AccessDeniedException("Usuario no autorizado para eliminar recetas");
        }
        recipeService.deleteRecipe(id);
        String deleteMessage = "Recipe deleted successfully";
        logger.info("Receta borrada exitosamente");
        return  ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }

    @PatchMapping("/recipes/{id}")
    public ResponseEntity<RecipeDto> updateRecipePartially(
            @PathVariable long id, @RequestBody Map<String, Object> updates)
            throws RecipeNotFoundException, UserNotFoundException {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User not found"));
        RecipeDto recipe = recipeService.findById(id);

        if (recipe.getUserId() != user.getId() && !user.isAdmin()) {
            logger.info("Usuario no autorizado para actualizar esta receta");
            throw new AccessDeniedException("Usuario no autorizado para actualizar recetas");
        }

        RecipeDto updatedRecipeDto = recipeService.updateRecipeByField(id, updates);
        logger.info("Datos de "+updatedRecipeDto.getName()+" actualizados");
        return ResponseEntity.status(HttpStatus.OK).body(updatedRecipeDto);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorMessage> handleAccessDeniedException(AccessDeniedException ade) {
        logger.error(ade.getMessage(), ade);
        ErrorMessage errorMessage = new ErrorMessage(403, ade.getMessage());
        return new ResponseEntity<>(errorMessage, HttpStatus.FORBIDDEN);
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
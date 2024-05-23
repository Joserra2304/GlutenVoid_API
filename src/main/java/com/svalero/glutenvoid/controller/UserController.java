package com.svalero.glutenvoid.controller;

import com.svalero.glutenvoid.domain.GlutenCondition;
import com.svalero.glutenvoid.domain.LoginRequest;
import com.svalero.glutenvoid.domain.User;
import com.svalero.glutenvoid.exception.ErrorMessage;
import com.svalero.glutenvoid.exception.UserNotFoundException;
import com.svalero.glutenvoid.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class UserController {

    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    UserService userService;

    @GetMapping("/users")
    public ResponseEntity<List<User>> getUsers(
            @RequestParam(name="glutenCondition", required = false, defaultValue = "") String glutenCondition,
            @RequestParam(name="isAdmin", required = false, defaultValue = "") String isAdmin)
            throws UserNotFoundException{

        if(!glutenCondition.isEmpty()){
            logger.info("Usuarios filtrados por tipo de condición");
            return ResponseEntity.ok(userService.filterByGlutenCondition(GlutenCondition.valueOf(glutenCondition.toUpperCase())));
        } else if (!isAdmin.isEmpty()) {
            logger.info("Usuarios filtrados por privilegio");
            return ResponseEntity.ok(userService.filterByAdmin(Boolean.parseBoolean(isAdmin)));
        } else{
            logger.info("Listado de Usuarios");
            return ResponseEntity.ok(userService.findAll());
        }
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable long id) throws UserNotFoundException {
        logger.info("Usuario mostrado con el id: "+id);
        return ResponseEntity.ok(userService.findById(id));
    }

    @PostMapping("/users")
    public  ResponseEntity<User> addUser(@Valid @RequestBody User user){
        User newUser = userService.addUser(user);
        logger.info(newUser.getName() + ", con ID:" + newUser.getId() + ", ha sido registrado");
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    @PostMapping("/users/login")
    public ResponseEntity<?> loginRequest(@RequestBody LoginRequest loginRequest) {
        Optional<User> userOpt = userService.loginRequest(loginRequest.getUsername(), loginRequest.getPassword());
        if (userOpt.isPresent()) {
            logger.info(loginRequest.getUsername() + " se ha logueado");
            return ResponseEntity.ok(userOpt.get());
        } else {
            logger.info("Credenciales incorrectas");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales incorrectas");
        }
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable long id) throws UserNotFoundException{
        userService.deleteUser(id);
        String deleteMessage = "User deleted successfully";
        logger.info("Usuario borrado exitosamenete");
        return  ResponseEntity.ok(deleteMessage);

    }

    @PatchMapping("/users/{id}")
    public ResponseEntity<User> updateUserPartially(@PathVariable long id, @RequestBody Map<String, Object> updates)
            throws UserNotFoundException{
        User updateUser = userService.updateUserByField(id, updates);

        logger.info("Datos de "+updateUser.getName()+" actualizados");
        return  ResponseEntity.ok(updateUser);
    }

    //EXCEPTION HANDLER

    @ExceptionHandler(UserNotFoundException.class)
    public  ResponseEntity<ErrorMessage> userNotFoundException(UserNotFoundException unfe){
        logger.error(unfe.getMessage(), unfe);
        ErrorMessage notFound = new ErrorMessage(404, unfe.getMessage());
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
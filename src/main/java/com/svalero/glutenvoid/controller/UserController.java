package com.svalero.glutenvoid.controller;

import com.svalero.glutenvoid.domain.enumeration.GlutenCondition;
import com.svalero.glutenvoid.domain.entity.User;
import com.svalero.glutenvoid.controller.request.LoginRequest;

import com.svalero.glutenvoid.exception.ErrorMessage;
import com.svalero.glutenvoid.exception.UserNotFoundException;
import com.svalero.glutenvoid.service.UserService;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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
            List<User> users = userService.filterByGlutenCondition(GlutenCondition.valueOf(
                    glutenCondition.toUpperCase()));
            logger.info("Usuarios filtrados por tipo de condición");
            return ResponseEntity.status(HttpStatus.OK).body(users);
        } else if (!isAdmin.isEmpty()) {
            logger.info("Usuarios filtrados por privilegio");
            List<User> users = userService.filterByAdmin(Boolean.parseBoolean(isAdmin));
            return ResponseEntity.status(HttpStatus.OK).body(users);
        } else{
            logger.info("Listado de Usuarios");
            List<User> users = userService.findAll();
            return ResponseEntity.status(HttpStatus.OK).body(users);
        }
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable long id) throws UserNotFoundException {
        User user = userService.findById(id);
        logger.info("Usuario mostrado con el id: "+id);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @PostMapping("/users")
    public ResponseEntity<?> addUser(@Valid @RequestBody User user) {
        try {
            User newUser = userService.addUser(user);
            logger.info(newUser.getName() + ", con ID:" + newUser.getId() + ", ha sido registrado");
            return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
        } catch (ConstraintViolationException e) {
            ErrorMessage errorMessage = new ErrorMessage(400, e.getMessage());
            return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
        } catch (DataIntegrityViolationException e) {
            ErrorMessage errorMessage = new ErrorMessage(400, "Username already exists");
            return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/users/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        String token = userService.loginRequest(loginRequest.getUsername(), loginRequest.getPassword());
        if (token != null) {
            logger.info(loginRequest.getUsername() + " se ha logueado");
            return ResponseEntity.ok(Map.of("jwt", token));
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
        return  ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }

    @PatchMapping("/users/{id}")
    public ResponseEntity<User> updateUserPartially(@PathVariable long id, @RequestBody Map<String, Object> updates)
            throws UserNotFoundException{
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User currentUser = userService.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User not found: " + username));
        User updateUser = userService.findById(id);

        if (updateUser == null) {
            throw new UserNotFoundException("User not found with id: " + id);
        }

        if (updateUser.getId() != currentUser.getId() && !currentUser.isAdmin()) {
            throw new AccessDeniedException("Usuario no autorizado para actualizar esta cuenta");
        }

        User updatedUser = userService.updateUserByField(id, updates);

        logger.info("Datos de "+updateUser.getName()+" actualizados");
        return  ResponseEntity.status(HttpStatus.OK).body(updatedUser);
    }

    //EXCEPTION HANDLER

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

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorMessage> handleDataIntegrityViolationException(DataIntegrityViolationException dive) {
        logger.error(dive.getMessage(), dive);
        ErrorMessage errorMessage = new ErrorMessage(400, "Username already exists");
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorMessage> handleConstraintViolationException(ConstraintViolationException cve) {
        logger.error(cve.getMessage(), cve);
        ErrorMessage errorMessage = new ErrorMessage(
                400, "Constraint violation: " + cve.getMessage());
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorMessage> handleAccessDeniedException(AccessDeniedException ade) {
        logger.error(ade.getMessage(), ade);
        ErrorMessage errorMessage = new ErrorMessage(403, ade.getMessage());
        return new ResponseEntity<>(errorMessage, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public  ResponseEntity<ErrorMessage> userNotFoundException(UserNotFoundException unfe){
        logger.error(unfe.getMessage(), unfe);
        ErrorMessage notFound = new ErrorMessage(404, unfe.getMessage());
        return new ResponseEntity<>(notFound, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessage> handleException(Exception e) {
        logger.error(e.getMessage(), e);
        ErrorMessage errorMessage = new ErrorMessage(500, "Error interno del servidor");
        return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
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
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) throws UserNotFoundException {
        String token = userService.loginRequest(loginRequest.getUsername(), loginRequest.getPassword());
        if (token != null) {
            User user = userService.findByUsername(loginRequest.getUsername()).orElse(null);
            if (user != null) {
                logger.info(loginRequest.getUsername() + " se ha logueado");
                Map<String, Object> response = new HashMap<>();
                response.put("jwt", token);
                response.put("user", Map.of(
                        "id", user.getId(),
                        "name", user.getName(),
                        "surname", user.getSurname(),
                        "email", user.getEmail(),
                        "username", user.getUsername(),
                        "profileBio", user.getProfileBio(),
                        "glutenCondition", user.getGlutenCondition().toString(),
                        "isAdmin", user.isAdmin()
                ));
                return ResponseEntity.ok(response);
            } else {
                logger.error("Error al recuperar los detalles del usuario después de la autenticación.");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al recuperar detalles del usuario");
            }
        } else {
            logger.info("Credenciales incorrectas");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales incorrectas");
        }
    }



    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable long id) {
        try {
            // Obtener el nombre de usuario del contexto de seguridad
            String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            // Buscar el usuario actual por su nombre de usuario
            User currentUser = userService.findByUsername(username)
                    .orElseThrow(() -> new UserNotFoundException("User not found: " + username));

            // Buscar el usuario que se desea eliminar por su ID
            User deleteUser = userService.findById(id);

            // Verificar si el usuario actual tiene permiso para eliminar la cuenta
            if (deleteUser.getId() != currentUser.getId() && !currentUser.isAdmin()) {
                logger.error("Usuario no autorizado para eliminar esta cuenta: " + username);
                throw new AccessDeniedException("Usuario no autorizado para eliminar esta cuenta");
            }

            // Eliminar el usuario
            userService.deleteUser(id);
            String deleteMessage = "User deleted successfully";
            logger.info("Usuario borrado exitosamente: " + deleteUser.getUsername());
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(deleteMessage);
        } catch (UserNotFoundException e) {
            logger.error("Error al intentar eliminar el usuario", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (AccessDeniedException e) {
            logger.error("Acceso denegado al intentar eliminar el usuario", e);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error inesperado al intentar eliminar el usuario", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor");
        }
    }


    @PatchMapping("/users/{id}")
    public ResponseEntity<User> updateUserPartially(@PathVariable long id, @RequestBody Map<String, Object> updates) {
        try {
            // Obtener el nombre de usuario del contexto de seguridad
            String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            // Buscar el usuario actual por su nombre de usuario
            User currentUser = userService.findByUsername(username)
                    .orElseThrow(() -> new UserNotFoundException("User not found: " + username));

            // Buscar el usuario que se desea actualizar por su ID
            User updateUser = userService.findById(id);

            // Verificar si el usuario actual tiene permiso para actualizar la cuenta
            if (updateUser.getId() != currentUser.getId() && !currentUser.isAdmin()) {
                logger.error("Usuario no autorizado para actualizar esta cuenta: " + username);
                throw new AccessDeniedException("Usuario no autorizado para actualizar esta cuenta");
            }

            // Actualizar el usuario
            User updatedUser = userService.updateUserByField(id, updates);
            logger.info("Datos de " + updatedUser.getName() + " actualizados");
            return ResponseEntity.status(HttpStatus.OK).body(updatedUser);
        } catch (UserNotFoundException e) {
            logger.error("Error al intentar actualizar el usuario", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (AccessDeniedException e) {
            logger.error("Acceso denegado al intentar actualizar el usuario", e);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        } catch (Exception e) {
            logger.error("Error inesperado al intentar actualizar el usuario", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
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
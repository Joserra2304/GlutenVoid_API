package com.svalero.glutenvoid.controller;

import com.svalero.glutenvoid.domain.dto.EstablishmentDto;
import com.svalero.glutenvoid.domain.entity.Establishment;
import com.svalero.glutenvoid.domain.entity.User;
import com.svalero.glutenvoid.exception.ErrorMessage;
import com.svalero.glutenvoid.exception.EstablishmentNotFoundException;
import com.svalero.glutenvoid.exception.UserNotFoundException;
import com.svalero.glutenvoid.service.EstablishmentService;
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
public class EstablishmentController {

    private final Logger logger = LoggerFactory.getLogger(EstablishmentController.class);

    @Autowired
    EstablishmentService establishmentService;
    @Autowired
    UserService userService;

    @GetMapping("/establishments")
    public ResponseEntity<List<EstablishmentDto>> getEstablishments(
            @RequestParam(name="city", required = false, defaultValue = "") String city,
            @RequestParam(name="glutenFree", required = false, defaultValue = "") String glutenFree)
            throws EstablishmentNotFoundException {

        if(!city.isEmpty()){
            List<EstablishmentDto> establishments = establishmentService.filterByCity(city);
            logger.info("Establecimientos filtrados por ciudad");
            return ResponseEntity.status(HttpStatus.OK).body(establishments);
        } else if (!glutenFree.isEmpty()) {
            List<EstablishmentDto> establishments = establishmentService
                    .filterByGlutenFree(Boolean.parseBoolean(glutenFree));
            logger.info("Establecimientos filtrados por opción sin gluten");
            return ResponseEntity.status(HttpStatus.OK).body(establishments);
        } else{
            List<EstablishmentDto> establishments = establishmentService.findAll();
            logger.info("Listado de Establecimientps");
            return ResponseEntity.status(HttpStatus.OK).body(establishments);
        }
    }

    @GetMapping("/establishments/{id}")
    public ResponseEntity<EstablishmentDto> getEstablishmentById(@PathVariable long id)
            throws EstablishmentNotFoundException{
        EstablishmentDto establishment = establishmentService.findById(id);
        logger.info("Establecimiento mostrado con el id: "+id);
        return ResponseEntity.status(HttpStatus.OK).body(establishment);
    }

    @PostMapping("/establishments")
    public ResponseEntity<EstablishmentDto> addEstablishment(@Valid @RequestBody EstablishmentDto establishmentdto)
            throws UserNotFoundException {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User not found"));

        if (!user.isAdmin()) {
            logger.info("Usuario no autorizado para agregar establecimientos");
            throw new AccessDeniedException("Usuario no autorizado para agregar establecimientos");
        }

        EstablishmentDto newEstablishment = establishmentService.addEstablishment(establishmentdto);
        logger.info(newEstablishment.getName() + ", con ID:" + newEstablishment.getId() + ", ha sido registrado");
        return ResponseEntity.status(HttpStatus.CREATED).body(newEstablishment);
    }

    @DeleteMapping("/establishments/{id}")
    public ResponseEntity<String> deleteEstablishment(@PathVariable long id) throws EstablishmentNotFoundException, UserNotFoundException {

        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User not found"));

        if (!user.isAdmin()) {
            logger.info("Usuario no autorizado para eliminar establecimientos");
            throw new AccessDeniedException("Usuario no autorizado para eliminar establecimientos");
        }

        establishmentService.deleteEstablishment(id);
        String deleteMessage = "Establishment deleted successfully";
        logger.info("Establecimiento borrado exitosamente");
        return  ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }

    @PatchMapping("/establishments/{id}")
            public ResponseEntity<EstablishmentDto> updateEstablishmentPartially(
                    @PathVariable long id, @RequestBody Map<String, Object> updates)
            throws EstablishmentNotFoundException, UserNotFoundException {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User not found"));

        if (!user.isAdmin()) {
            logger.info("Usuario no autorizado para actualizar establecimientos");
            throw new AccessDeniedException("Usuario no autorizado para actualizar establecimientos");
        }

       EstablishmentDto updateEstablishment = establishmentService.updateEstablishmentByField(id, updates);
        logger.info("Datos de " + updateEstablishment.getName() + " actualizados");
        return ResponseEntity.status(HttpStatus.OK).body(updateEstablishment);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorMessage> handleAccessDeniedException(AccessDeniedException ade) {
        logger.error(ade.getMessage(), ade);
        ErrorMessage errorMessage = new ErrorMessage(403, ade.getMessage());
        return new ResponseEntity<>(errorMessage, HttpStatus.FORBIDDEN);
    }


    @ExceptionHandler(EstablishmentNotFoundException.class)
    public  ResponseEntity<ErrorMessage> establishmentNotFoundException(EstablishmentNotFoundException enfe){
       logger.error(enfe.getMessage(), enfe);
        ErrorMessage notFound = new ErrorMessage(404, enfe.getMessage());
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

package com.svalero.glutenvoid.controller;

import com.svalero.glutenvoid.domain.entity.Establishment;
import com.svalero.glutenvoid.exception.ErrorMessage;
import com.svalero.glutenvoid.exception.EstablishmentNotFoundException;
import com.svalero.glutenvoid.service.EstablishmentService;
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
public class EstablishmentController {

    private final Logger logger = LoggerFactory.getLogger(EstablishmentController.class);

    @Autowired
    EstablishmentService establishmentService;

    @GetMapping("/establishments")
    public ResponseEntity<List<Establishment>> getEstablishments(
            @RequestParam(name="city", required = false, defaultValue = "") String city,
            @RequestParam(name="glutenFree", required = false, defaultValue = "") String glutenFree)
            throws EstablishmentNotFoundException {

        if(!city.isEmpty()){
            logger.info("Establecimientos filtrados por ciudad");
            return ResponseEntity.ok(establishmentService.filterByCity(city));
        } else if (!glutenFree.isEmpty()) {
            logger.info("Establecimientos filtrados por opción sin gluten");
            return ResponseEntity.ok(establishmentService.filterByGlutenFree(Boolean.parseBoolean(glutenFree)));
        } else{
            logger.info("Listado de Establecimientps");
            return ResponseEntity.ok(establishmentService.findAll());
        }
    }

    @GetMapping("/establishments/{id}")
    public ResponseEntity<Establishment> getEstablishmentById(@PathVariable long id) throws EstablishmentNotFoundException{
        logger.info("Establecimiento mostrado con el id: "+id);
        return ResponseEntity.ok(establishmentService.findById(id));
    }

    @PostMapping("/establishments")
    public ResponseEntity<Establishment> addEstablishment(@Valid @RequestBody Establishment establishment){
        Establishment newEstablishment = establishmentService.addEstablishment(establishment);
        logger.info(newEstablishment.getName() + ", con ID:" + newEstablishment.getId() + ", ha sido registrado");
        return ResponseEntity.ok(newEstablishment);
    }

    @DeleteMapping("/establishments/{id}")
    public ResponseEntity<String> deleteEstablishment(@PathVariable long id) throws EstablishmentNotFoundException{
        establishmentService.deleteEstablishment(id);

        String deleteMessage = "Establishment deleted successfully";
        logger.info("Establecimiento borrado exitosamente");
        return  ResponseEntity.ok(deleteMessage);

    }

    @PatchMapping("/establishments/{id}")
            public ResponseEntity<Establishment> updateEstablishmentPartially(
                    @PathVariable long id, @RequestBody Map<String, Object> updates)
            throws EstablishmentNotFoundException{
       Establishment updateEstablishment = establishmentService.updateEstablishmentByField(id, updates);
        logger.info("Datos de " + updateEstablishment.getName() + " actualizados");
        return ResponseEntity.ok(updateEstablishment);
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

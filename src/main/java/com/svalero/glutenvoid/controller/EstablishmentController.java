package com.svalero.glutenvoid.controller;

import com.svalero.glutenvoid.domain.Establishment;
import com.svalero.glutenvoid.domain.Product;
import com.svalero.glutenvoid.exception.ErrorMessage;
import com.svalero.glutenvoid.exception.EstablishmentNotFoundException;
import com.svalero.glutenvoid.exception.UserNotFoundException;
import com.svalero.glutenvoid.service.EstablishmentService;
import jakarta.validation.Valid;
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

    @Autowired
    EstablishmentService establishmentService;

    @GetMapping("/establishments")
    public ResponseEntity<List<Establishment>> getEstablishments(
            @RequestParam(name="city", required = false, defaultValue = "") String city,
            @RequestParam(name="glutenFree", required = false, defaultValue = "") String glutenFree)
            throws EstablishmentNotFoundException {

        if(!city.isEmpty()){
            return ResponseEntity.ok(establishmentService.filterByCity(city));
        } else if (!glutenFree.isEmpty()) {
            return ResponseEntity.ok(establishmentService.filterByGlutenFree(Boolean.parseBoolean(glutenFree)));
        } else{
            return ResponseEntity.ok(establishmentService.findAll());
        }
    }

    @GetMapping("/establishments/{id}")
    public ResponseEntity<Establishment> getEstablishmentById(@PathVariable long id) throws EstablishmentNotFoundException{
        return ResponseEntity.ok(establishmentService.findById(id));
    }

    @PostMapping("/establishments")
    public ResponseEntity<Establishment> addEstablishment(@Valid @RequestBody Establishment establishment){
        Establishment newEstablishment = establishmentService.addEstablishment(establishment);
        return ResponseEntity.ok(newEstablishment);
    }

    @DeleteMapping("/establishments/{id}")
    public ResponseEntity<String> deleteEstablishment(@PathVariable long id) throws EstablishmentNotFoundException{
        establishmentService.deleteEstablishment(id);

        String deleteMessage = "Establishment deleted successfully";
        return  ResponseEntity.ok(deleteMessage);

    }

    @PatchMapping("/establishments/{id}")
            public ResponseEntity<Establishment> updateEstablishmentPartially(
                    @PathVariable long id, @RequestBody Map<String, Object> updates)
            throws EstablishmentNotFoundException{
       Establishment updateEstablishment = establishmentService.updateEstablishmentByField(id, updates);
        return ResponseEntity.ok(updateEstablishment);
    }

    @ExceptionHandler(EstablishmentNotFoundException.class)
    public  ResponseEntity<ErrorMessage> establishmentNotFoundException(EstablishmentNotFoundException enfe){
        //Logger
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

        //Logger
        ErrorMessage badRequest = new ErrorMessage(400, "Bad Request", errors);
        return new ResponseEntity<>(badRequest, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessage> handleException(Exception e) {
        //Logger
        ErrorMessage errorMessage = new ErrorMessage(500, "Internal Server Error");
        return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}

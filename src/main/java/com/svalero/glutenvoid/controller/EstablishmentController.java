package com.svalero.glutenvoid.controller;

import com.svalero.glutenvoid.domain.Establishment;
import com.svalero.glutenvoid.domain.Product;
import com.svalero.glutenvoid.service.EstablishmentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class EstablishmentController {

    @Autowired
    EstablishmentService establishmentService;

    @GetMapping("/establishments")
    public ResponseEntity<List<Establishment>> getEstablishments(
            @RequestParam(name="city", required = false, defaultValue = "") String city,
            @RequestParam(name="glutenFree", required = false, defaultValue = "") String glutenFree){

        if(!city.isEmpty()){
            return ResponseEntity.ok(establishmentService.filterByCity(city));
        } else if (!glutenFree.isEmpty()) {
            return ResponseEntity.ok(establishmentService.filterByGlutenFree(Boolean.parseBoolean(glutenFree)));
        } else{
            return ResponseEntity.ok(establishmentService.findAll());
        }
    }

    @GetMapping("/establishments/{id}")
    public ResponseEntity<Establishment> getEstablishmentById(@PathVariable long id){
        return ResponseEntity.ok(establishmentService.findById(id));
    }

    @PostMapping("/establishments")
    public ResponseEntity<Establishment> addEstablishment(@Valid @RequestBody Establishment establishment){
        Establishment newEstablishment = establishmentService.addEstablishment(establishment);
        return ResponseEntity.ok(newEstablishment);
    }

    @DeleteMapping("/establishments/{id}")
    public ResponseEntity<String> deleteEstablishment(@PathVariable long id){
        establishmentService.deleteEstablishment(id);

        String deleteMessage = "Establishment deleted successfully";
        return  ResponseEntity.ok(deleteMessage);

    }

    @PatchMapping("/establishments/{id}")
            public ResponseEntity<Establishment> updateEstablishmentPartially(@PathVariable long id, @RequestBody Map<String, Object> updates){
       Establishment updateEstablishment = establishmentService.updateEstablishmentByField(id, updates);
        return ResponseEntity.ok(updateEstablishment);
    }
}

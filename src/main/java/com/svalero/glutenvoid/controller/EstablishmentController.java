package com.svalero.glutenvoid.controller;

import com.svalero.glutenvoid.domain.Establishment;
import com.svalero.glutenvoid.service.EstablishmentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PostMapping("/establishments")
    public ResponseEntity<Establishment> addEstablishment(@Valid @RequestBody Establishment establishment){
        Establishment newEstablishment = establishmentService.addEstablishment(establishment);
        return ResponseEntity.ok(newEstablishment);
    }
}

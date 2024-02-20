package com.svalero.glutenvoid.controller;

import com.svalero.glutenvoid.domain.GlutenCondition;
import com.svalero.glutenvoid.domain.User;
import com.svalero.glutenvoid.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/users")
    public ResponseEntity<List<User>> getUsers(
            @RequestParam(name="glutenCondition", required = false, defaultValue = "") String glutenCondition,
            @RequestParam(name="isAdmin", required = false, defaultValue = "") String isAdmin){

        if(!glutenCondition.isEmpty()){
            return ResponseEntity.ok(userService.filterByGlutenCondition(GlutenCondition.valueOf(glutenCondition.toUpperCase())));
        } else if (!isAdmin.isEmpty()) {
            return ResponseEntity.ok(userService.filterByAdmin(Boolean.parseBoolean(isAdmin)));
        } else{
            return ResponseEntity.ok(userService.getAll());
        }
    }

}

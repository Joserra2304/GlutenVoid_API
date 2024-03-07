package com.svalero.glutenvoid.controller;

import com.svalero.glutenvoid.domain.GlutenCondition;
import com.svalero.glutenvoid.domain.User;
import com.svalero.glutenvoid.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
            return ResponseEntity.ok(userService.findAll());
        }
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable long id){
        return ResponseEntity.ok(userService.findById(id));
    }

    @PostMapping("/users")
    public  ResponseEntity<User> addUser(@Valid @RequestBody User user){

        User newUser = userService.addUser(user);
        return ResponseEntity.ok(newUser);
    }

}

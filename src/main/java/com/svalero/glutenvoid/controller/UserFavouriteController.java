package com.svalero.glutenvoid.controller;

import com.svalero.glutenvoid.domain.GlutenCondition;
import com.svalero.glutenvoid.domain.User;
import com.svalero.glutenvoid.domain.dto.UserFavouriteDto;
import com.svalero.glutenvoid.domain.heritages.EstablishmentFavourite;
import com.svalero.glutenvoid.domain.heritages.RecipeFavourite;
import com.svalero.glutenvoid.domain.heritages.UserFavourite;
import com.svalero.glutenvoid.service.UserFavouriteService;
import com.svalero.glutenvoid.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class UserFavouriteController {

    @Autowired
    UserFavouriteService userFavouriteService;

    @GetMapping("/userFavourites")
    public ResponseEntity<?> getUserFavourites(
            @RequestParam(name = "id", required = true) Long userId,
            @RequestParam(name = "favouriteType", required = false) String favouriteType) {

        if (favouriteType != null && !favouriteType.isEmpty()) {
            switch (favouriteType.toLowerCase()) {
                case "establishment":
                    List<EstablishmentFavourite> establishmentFavourites = userFavouriteService.filterByEstablishment(userId);
                    return ResponseEntity.ok(establishmentFavourites);
                case "recipe":
                    List<RecipeFavourite> recipeFavourites = userFavouriteService.filterByRecipe(userId);
                    return ResponseEntity.ok(recipeFavourites);
                default:
                    return ResponseEntity.badRequest().body("Invalid favourite type specified.");
            }
        } else {
            List<UserFavourite> userFavourites = userFavouriteService.filterByUser(userId);
            return ResponseEntity.ok(userFavourites);
        }
    }

    @PostMapping("/userFavourites")
    public ResponseEntity<UserFavourite> addUserFavourite(@RequestBody UserFavouriteDto userFavouriteDto) {
        UserFavourite savedUserFavourite = userFavouriteService.addFavouriteUser(userFavouriteDto);
        return ResponseEntity.ok(savedUserFavourite);
    }
}

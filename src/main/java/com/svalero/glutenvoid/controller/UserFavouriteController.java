package com.svalero.glutenvoid.controller;

import com.svalero.glutenvoid.domain.Establishment;
import com.svalero.glutenvoid.domain.GlutenCondition;
import com.svalero.glutenvoid.domain.Recipe;
import com.svalero.glutenvoid.domain.User;
import com.svalero.glutenvoid.domain.dto.UserFavouriteDto;
import com.svalero.glutenvoid.domain.dto.UserFavouriteResponseDto;
import com.svalero.glutenvoid.domain.heritages.EstablishmentFavourite;
import com.svalero.glutenvoid.domain.heritages.RecipeFavourite;
import com.svalero.glutenvoid.domain.heritages.UserFavourite;
import com.svalero.glutenvoid.service.UserFavouriteService;
import com.svalero.glutenvoid.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class UserFavouriteController {

    @Autowired
    UserFavouriteService userFavouriteService;

    @GetMapping("/userFavourites")
    public ResponseEntity<List<UserFavouriteResponseDto>> getAllFavourites() {
        List<UserFavourite> favourites = userFavouriteService.findAll();
        List<UserFavouriteResponseDto> responseFavourites = new ArrayList<>();

        for (UserFavourite favourite : favourites){
            UserFavouriteResponseDto responseApi = new UserFavouriteResponseDto();
            responseApi.setId(favourite.getId());
            responseApi.setUserId(favourite.getUser().getId());
            responseApi.setUsername(favourite.getUser().getUsername());

            if(favourite instanceof RecipeFavourite){
                Recipe recipe = ((RecipeFavourite) favourite).getRecipe();
                if(recipe != null){
                    responseApi.setRecipeId(recipe.getId());
                    responseApi.setRecipeName(recipe.getName());
                } else {
                    // Agregar log aquí para saber si recipe es null
                    System.out.println("Recipe is null for favourite id: " + favourite.getId());
                }
            }

            if (favourite instanceof EstablishmentFavourite){
                Establishment establishment = ((EstablishmentFavourite) favourite).getEstablishment();
                if(establishment != null){
                    responseApi.setEstablishmentId(establishment.getId());
                    responseApi.setEstablishmentName(establishment.getName());
                }
            }
            responseFavourites.add(responseApi);
        }

        return ResponseEntity.ok(responseFavourites);
    }

    @GetMapping("/userFavourites/{id}")
    public ResponseEntity<?> getUserFavourites(
            @RequestParam(name = "id", required = true) Long userId,
            @RequestParam(name = "favouriteType", required = false) String type) {

      if ("establishment".equalsIgnoreCase(type)) {
          return ResponseEntity.ok(userFavouriteService.filterByEstablishment(userId));
      } else if ("recipe".equalsIgnoreCase(type)){
          return  ResponseEntity.ok(userFavouriteService.filterByRecipe(userId));
      }

        return ResponseEntity.ok(userFavouriteService.filterByUser(userId));
    }

    @PostMapping("/userFavourites")
    public ResponseEntity<UserFavouriteResponseDto> addUserFavourite(@RequestBody UserFavouriteDto userFavouriteDto) {
        UserFavourite favourite = userFavouriteService.addFavouriteUser(userFavouriteDto);

        UserFavouriteResponseDto responseApi = new UserFavouriteResponseDto();

        responseApi.setId(favourite.getId());
        responseApi.setUserId(favourite.getUser().getId());
        responseApi.setUsername(favourite.getUser().getUsername());
        if(favourite instanceof  RecipeFavourite){
            RecipeFavourite recipeFavourite= (RecipeFavourite) favourite;
            if(recipeFavourite.getRecipe() != null){
                responseApi.setRecipeId(recipeFavourite.getRecipe().getId());
                responseApi.setRecipeName(recipeFavourite.getRecipe().getName());

            }
        }
        if(favourite instanceof  EstablishmentFavourite){
            EstablishmentFavourite establishmentFavourite = (EstablishmentFavourite) favourite;
            if(establishmentFavourite.getEstablishment() != null){
                responseApi.setRecipeId(establishmentFavourite.getEstablishment().getId());
                responseApi.setRecipeName(establishmentFavourite.getEstablishment().getName());

            }
        }

        return ResponseEntity.ok(responseApi);
    }
}

package com.svalero.glutenvoid.service;

import com.svalero.glutenvoid.domain.Establishment;
import com.svalero.glutenvoid.domain.Recipe;
import com.svalero.glutenvoid.domain.User;
import com.svalero.glutenvoid.domain.dto.UserFavouriteDto;
import com.svalero.glutenvoid.domain.heritages.EstablishmentFavourite;
import com.svalero.glutenvoid.domain.heritages.RecipeFavourite;
import com.svalero.glutenvoid.domain.heritages.UserFavourite;
import com.svalero.glutenvoid.repository.EstablishmentRepository;
import com.svalero.glutenvoid.repository.RecipeRepository;
import com.svalero.glutenvoid.repository.UserFavouriteRepository;
import com.svalero.glutenvoid.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserFavouriteServiceImplementation implements UserFavouriteService {

    @Autowired
    UserFavouriteRepository userFavouriteRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EstablishmentRepository establishmentRepository;

    @Autowired
    private RecipeRepository recipeRepository;


    @Override
    public List<UserFavourite> findAll() {
        return userFavouriteRepository.findAll();
    }

    @Override
    public List<UserFavourite> filterByUser(Long user_id) {
        return userFavouriteRepository.findByUserId(user_id);
    }

    @Override
    public List<EstablishmentFavourite> filterByEstablishment(Long user_id) {
        return userFavouriteRepository.findEstablishmentFavouritesByUserId(user_id);
    }

    @Override
    public List<RecipeFavourite> filterByRecipe(Long user_id) {
        return userFavouriteRepository.findRecipeFavouritesByUserId(user_id);
    }


    @Override
    public UserFavourite addFavouriteUser(UserFavouriteDto userFavouriteDto) {

        if(userFavouriteDto.getEstablishmentId() != null){
            Establishment establishment = establishmentRepository.findById(userFavouriteDto.getEstablishmentId()).orElse(null);
            User user = userRepository.findById(userFavouriteDto.getUserId()).orElse(null);

            if(establishment != null && user != null){
                EstablishmentFavourite favourite = new EstablishmentFavourite();
                favourite.setUser(user);
                favourite.setEstablishment(establishment);
                return userFavouriteRepository.save(favourite);
            }
        } else if (userFavouriteDto.getRecipeId() != null){
            Recipe recipe = recipeRepository.findById(userFavouriteDto.getRecipeId()).orElse(null);
            User user = userRepository.findById(userFavouriteDto.getUserId()).orElse(null);

            if(recipe != null && user != null){
                RecipeFavourite favourite = new RecipeFavourite();
                favourite.setUser(user);
                favourite.setRecipe(recipe);
                return userFavouriteRepository.save(favourite);
            }
        }
        return null;
    }


}
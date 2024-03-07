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
        User user = userRepository.findById(userFavouriteDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));

        if(userFavouriteDto.getEstablishmentId() != null){
            Establishment establishment = establishmentRepository.findById(userFavouriteDto.getEstablishmentId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid establishment ID"));

            EstablishmentFavourite favourite = new EstablishmentFavourite();
            favourite.setUser(user);
            favourite.setEstablishment(establishment);
            return userFavouriteRepository.save(favourite);

        }

        if (userFavouriteDto.getRecipeId() != null){
            Recipe recipe = recipeRepository.findById(userFavouriteDto.getRecipeId()).
                orElseThrow(() -> new IllegalArgumentException("Invalid recipe ID"));

            RecipeFavourite favourite = new RecipeFavourite();
            favourite.setUser(user);
            favourite.setRecipe(recipe);
            return userFavouriteRepository.save(favourite);

        }
        throw new IllegalArgumentException("Favourite details are not specified");
    }

    @Override
    public void deleteFavourite(long id) {
        UserFavourite deleteFavourite = userFavouriteRepository.findById(id).orElseThrow();
        userFavouriteRepository.delete(deleteFavourite);
    }

}



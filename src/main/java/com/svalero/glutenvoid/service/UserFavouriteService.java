package com.svalero.glutenvoid.service;


import com.svalero.glutenvoid.domain.User;
import com.svalero.glutenvoid.domain.dto.UserFavouriteDto;
import com.svalero.glutenvoid.domain.heritages.EstablishmentFavourite;
import com.svalero.glutenvoid.domain.heritages.RecipeFavourite;
import com.svalero.glutenvoid.domain.heritages.UserFavourite;

import java.util.List;

public interface UserFavouriteService {

    List<UserFavourite> findAll();

    List<UserFavourite> filterByUser(Long user_id);

    List<EstablishmentFavourite> filterByEstablishment(Long userId);

    List<RecipeFavourite> filterByRecipe(Long userId);

    UserFavourite addFavouriteUser(UserFavouriteDto userFavouriteDto);

    void deleteFavourite(long id);



}

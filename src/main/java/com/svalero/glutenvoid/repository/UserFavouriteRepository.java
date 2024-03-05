package com.svalero.glutenvoid.repository;

import com.svalero.glutenvoid.domain.User;
import com.svalero.glutenvoid.domain.heritages.EstablishmentFavourite;
import com.svalero.glutenvoid.domain.heritages.RecipeFavourite;
import com.svalero.glutenvoid.domain.heritages.UserFavourite;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserFavouriteRepository extends CrudRepository<UserFavourite, Long> {

    List<UserFavourite> findAll();

    List<UserFavourite> findByUserId(Long user_id);

    List<EstablishmentFavourite> findEstablishmentFavouritesByUserId(Long user_id);

    List<RecipeFavourite> findRecipeFavouritesByUserId(Long user_id);

}

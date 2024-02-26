package com.svalero.glutenvoid.repository;

import com.svalero.glutenvoid.domain.Establishment;
import com.svalero.glutenvoid.domain.User;
import com.svalero.glutenvoid.domain.heritages.UserFavourite;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserFavouriteRepository extends CrudRepository<UserFavourite, Long> {

    List<UserFavourite> findAll();

    List<UserFavourite> findByUser(User user);

    List<UserFavourite> findByEstablishment (Establishment establishment);

}

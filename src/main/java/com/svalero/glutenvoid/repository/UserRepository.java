package com.svalero.glutenvoid.repository;

import com.svalero.glutenvoid.domain.GlutenCondition;
import com.svalero.glutenvoid.domain.User;
import com.svalero.glutenvoid.exception.UserNotFoundException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User,Long> {

    List<User> findAll();



    List<User> findByGlutenCondition(GlutenCondition glutenCondition) throws UserNotFoundException;

    List<User> findByAdmin(boolean isAdmin) throws UserNotFoundException;

}

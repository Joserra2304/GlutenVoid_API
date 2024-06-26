package com.svalero.glutenvoid.repository;

import com.svalero.glutenvoid.domain.enumeration.GlutenCondition;
import com.svalero.glutenvoid.domain.entity.User;
import com.svalero.glutenvoid.exception.UserNotFoundException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User,Long> {

    List<User> findAll();

    List<User> findByGlutenCondition(GlutenCondition glutenCondition) throws UserNotFoundException;

    List<User> findByAdmin(boolean isAdmin) throws UserNotFoundException;

    List<User> findByName(String name);

    Optional<User> findByUsername(String username);
}


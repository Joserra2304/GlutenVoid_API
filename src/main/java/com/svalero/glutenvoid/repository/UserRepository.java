package com.svalero.glutenvoid.repository;

import com.svalero.glutenvoid.domain.GlutenCondition;
import com.svalero.glutenvoid.domain.User;
import com.svalero.glutenvoid.exception.UserNotFoundException;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User,Long> {

    List<User> findAll();

    List<User> findByGlutenCondition(GlutenCondition glutenCondition) throws UserNotFoundException;

    List<User> findByAdmin(boolean isAdmin) throws UserNotFoundException;

    Optional<User> findByUsernameAndPassword(String username, String password);

    List<User> findByName(String name);

}

package com.svalero.glutenvoid.service;

import com.svalero.glutenvoid.domain.enumeration.GlutenCondition;
import com.svalero.glutenvoid.domain.entity.User;
import com.svalero.glutenvoid.exception.UserNotFoundException;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface UserService {
    List<User> findAll();

    User findById(long id) throws UserNotFoundException;

    List<User> filterByAdmin(boolean isAdmin) throws UserNotFoundException;
    List<User> filterByGlutenCondition(GlutenCondition glutenCondition) throws UserNotFoundException;

    User addUser(User user);
    void deleteUser(long id)throws UserNotFoundException;
    User updateUserByField(long id, Map<String, Object> updates) throws UserNotFoundException;

    Optional<User> loginRequest(String username, String password);

    List<User> findByName(String name) throws UserNotFoundException;
}

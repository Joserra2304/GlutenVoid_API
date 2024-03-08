package com.svalero.glutenvoid.service;

import com.svalero.glutenvoid.domain.GlutenCondition;
import com.svalero.glutenvoid.domain.User;

import java.util.List;
import java.util.Map;

public interface UserService {


    List<User> findAll();

    User findById(long id);

    List<User> filterByAdmin(boolean isAdmin);
    List<User> filterByGlutenCondition(GlutenCondition glutenCondition);

    User addUser(User user);

    void deleteUser(long id);

    User updateUser(long id, User updateUser);

    User updateUserByField(long id, Map<String, Object> updates);


}

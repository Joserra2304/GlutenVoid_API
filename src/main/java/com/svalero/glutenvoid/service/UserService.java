package com.svalero.glutenvoid.service;

import com.svalero.glutenvoid.domain.GlutenCondition;
import com.svalero.glutenvoid.domain.User;
import com.svalero.glutenvoid.domain.heritages.UserFavourite;

import java.util.List;

public interface UserService {

    List<User> findAll();

    User findById(long id);

    List<User> filterByAdmin(boolean isAdmin);
    List<User> filterByGlutenCondition(GlutenCondition glutenCondition);

    User addUser(User user);
}

package com.svalero.glutenvoid.service;

import com.svalero.glutenvoid.domain.GlutenCondition;
import com.svalero.glutenvoid.domain.User;
import com.svalero.glutenvoid.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImplementation implements UserService {

    @Autowired
    UserRepository userRepository;

    @Override
    public List<User> getAll() {
        return userRepository.getAll();
    }

    @Override
    public List<User> filterByAdmin(boolean isAdmin) {
        return userRepository.getByAdmin(isAdmin);
    }

    @Override
    public List<User> filterByGlutenCondition(GlutenCondition glutenCondition) {
        return userRepository.getByGlutenCondition(glutenCondition);
    }
}

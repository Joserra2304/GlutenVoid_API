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
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findById(long id) {
        return userRepository.findById(id).orElseThrow();
    }


    @Override
    public List<User> filterByAdmin(boolean isAdmin) {
        return userRepository.findByAdmin(isAdmin);
    }

    @Override
    public List<User> filterByGlutenCondition(GlutenCondition glutenCondition) {
        return userRepository.findByGlutenCondition(glutenCondition);
    }

    @Override
    public User addUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(long id) {
        User deleteUser = userRepository.findById(id).orElseThrow();
        userRepository.delete(deleteUser);
    }
}

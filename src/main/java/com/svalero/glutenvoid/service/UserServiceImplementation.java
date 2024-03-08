package com.svalero.glutenvoid.service;

import com.svalero.glutenvoid.domain.GlutenCondition;
import com.svalero.glutenvoid.domain.User;
import com.svalero.glutenvoid.exception.UserNotFoundException;
import com.svalero.glutenvoid.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class UserServiceImplementation implements UserService {

    @Autowired
    UserRepository userRepository;

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findById(long id) throws UserNotFoundException {
        return userRepository.findById(id).orElseThrow(UserNotFoundException::new);
    }


    @Override
    public List<User> filterByAdmin(boolean isAdmin) throws UserNotFoundException {
        return userRepository.findByAdmin(isAdmin);
    }

    @Override
    public List<User> filterByGlutenCondition(GlutenCondition glutenCondition) throws UserNotFoundException {
        return userRepository.findByGlutenCondition(glutenCondition);
    }

    @Override
    public User addUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(long id) throws UserNotFoundException {
        User deleteUser = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        userRepository.delete(deleteUser);
    }

    @Override
    public User updateUser(long id, User updateUser) throws UserNotFoundException {

        User updatedUser = userRepository.findById(id).orElseThrow(UserNotFoundException::new);

            updatedUser.setName(updateUser.getName());
            updatedUser.setSurname(updateUser.getSurname());
            updatedUser.setUsername(updateUser.getUsername());
            updatedUser.setEmail(updateUser.getEmail());
            updatedUser.setProfileBio(updateUser.getProfileBio());
            updatedUser.setGlutenCondition(updateUser.getGlutenCondition());

            return userRepository.save(updatedUser);

    }

    @Override
    public User updateUserByField(long id, Map<String, Object> updates) throws UserNotFoundException {

        User newUpdate = findById(id);

        updates.forEach((key, value) -> {
            switch (key) {
                case "name":
                    newUpdate.setName((String) value);
                    break;
                case"surname":
                    newUpdate.setSurname((String) value);
                    break;
                case"email":
                    newUpdate.setEmail((String) value);
                    break;
                case"profileBio":
                    newUpdate.setProfileBio((String) value);
                    break;
                case"admin":
                    newUpdate.setAdmin(Boolean.parseBoolean(value.toString()));
                    break;
                case"glutenCondition":
                    GlutenCondition glutenCondition = GlutenCondition.valueOf((String) value);
                    newUpdate.setGlutenCondition(glutenCondition);
                    break;
            }
        });

        return userRepository.save(newUpdate);
    }


}

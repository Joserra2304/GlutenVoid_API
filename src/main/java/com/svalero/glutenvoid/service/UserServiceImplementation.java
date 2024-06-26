package com.svalero.glutenvoid.service;

import com.svalero.glutenvoid.config.JwtTokenProvider;
import com.svalero.glutenvoid.domain.enumeration.GlutenCondition;
import com.svalero.glutenvoid.domain.entity.User;
import com.svalero.glutenvoid.exception.UserNotFoundException;
import com.svalero.glutenvoid.repository.UserRepository;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Service
public class UserServiceImplementation implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;


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
    public List<User> filterByGlutenCondition(GlutenCondition gluten_condition) throws UserNotFoundException {
        return userRepository.findByGlutenCondition(gluten_condition);
    }

    @Override
    public User addUser(User user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(long id) throws UserNotFoundException {
        User deleteUser = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        userRepository.delete(deleteUser);
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

    @Override
    public Map<String, Object> loginRequest(String username, String password) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent() && passwordEncoder.matches(password, userOpt.get().getPassword())) {
            User user = userOpt.get();
            Map<String, Object> tokenData = jwtTokenProvider.createToken(username, user.isAdmin());
            String token = (String) tokenData.get("token");
            long expiresAt = (long) tokenData.get("expiresAt");

            Map<String, Object> response = new HashMap<>();
            response.put("jwt", token);
            response.put("expiresAt", expiresAt); // Añadir el tiempo de expiración
            response.put("user", user); // Devolver el objeto User
            return response;
        }
        return null;
    }




    @Override
    public List<User> findByName(String name) throws UserNotFoundException {
        List<User> users = userRepository.findByName(name);
        if (users.isEmpty()) {
            throw new UserNotFoundException("User not found");
        }
        return users;
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
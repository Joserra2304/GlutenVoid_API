package com.svalero.glutenvoid.controller;

import com.svalero.glutenvoid.config.JwtTokenProvider;
import com.svalero.glutenvoid.domain.entity.User;
import com.svalero.glutenvoid.exception.UserNotFoundException;
import com.svalero.glutenvoid.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserService userService;

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestParam String token) {
        try {
            if (jwtTokenProvider.validateToken(token)) {
                String username = jwtTokenProvider.getUsername(token);
                User user = userService.findByUsername(username)
                        .orElseThrow(() -> new UserNotFoundException("User not found: " + username));
                Map<String, Object> newTokenData = jwtTokenProvider.createToken(username, user.isAdmin());
                return ResponseEntity.ok(newTokenData);
            } else {
                return ResponseEntity.status(401).body("Invalid or expired token");
            }
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }
}

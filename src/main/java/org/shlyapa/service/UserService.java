package org.shlyapa.service;

import org.shlyapa.model.User;
import org.shlyapa.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean register(String username, String password) {
        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            return false;
        }
        if (userRepository.findByUsername(username.trim()).isPresent()) {
            return false;
        }
        User user = new User(username.trim(), password.trim());
        userRepository.save(user);
        return true;
    }

    public boolean authenticate(String username, String password) {
        if (username == null || password == null) {
            return false;
        }
        Optional<User> userOptional = userRepository.findByUsername(username.trim());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return user.getPassword().equals(password.trim());
        }
        return false;
    }
}

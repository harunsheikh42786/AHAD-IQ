package com.ahad.serviceImpl;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ahad.entities.User;
import com.ahad.repository.UserRepository;
import com.ahad.services.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User createUser(User user) {
        user.setId(UUID.randomUUID().toString());
        user.setRegisteredTime(LocalDateTime.now());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEnable(true);
        return this.userRepository.save(user);
    }

    @Override
    public User updateUser(User user, Principal principal) {
        return this.userRepository.save(user);
    }

    @Override
    public User getUserByEmail(String email) {
        return this.userRepository.findUserByEmail(email);
    }

    @Override
    public User getUser(String id) {
        return this.userRepository.findById(id).get();
    }

    @Override
    public boolean deleteUser(String id) {
        this.userRepository.deleteById(id);
        return this.userRepository.findById(id) != null ? false : true;
    }

}

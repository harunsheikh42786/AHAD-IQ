package com.ahad.services;

import java.security.Principal;

import com.ahad.entities.User;

public interface UserService {

    public User createUser(User user);

    public User updateUser(User user, Principal principal);

    public User getUserByEmail(String email);

    public User getUser(String id);

    public boolean deleteUser(String id);

}

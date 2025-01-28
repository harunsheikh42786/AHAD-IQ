package com.ahad.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ahad.entities.User;

public interface UserRepository extends JpaRepository<User, String> {

    User findUserByEmail(String email);

}

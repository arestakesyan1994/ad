package com.example.advencedblog.repository;

import com.example.advencedblog.model.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Integer> {

    User findOneByEmail(String email);

    User findUserByEmail(String email);
}

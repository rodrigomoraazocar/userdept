package com.roroapi.userdept.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.roroapi.userdept.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByName(String name);

}

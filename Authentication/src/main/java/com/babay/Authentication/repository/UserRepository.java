package com.babay.Authentication.repository;

import com.babay.Authentication.Model.Role;
import com.babay.Authentication.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
    User findByRole(Role role);
    boolean existsByUsername(String username);
}

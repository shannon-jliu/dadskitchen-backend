package com.dadskitchen.repository;

import com.dadskitchen.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByNameIgnoreCase(String name);
}

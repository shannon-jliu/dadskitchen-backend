package com.dadskitchen.repository;

import com.dadskitchen.model.Chef;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChefRepository extends JpaRepository<Chef, Long> {
    Chef findByNameIgnoreCase(String name);

}

package com.dadskitchen.repository;

import com.dadskitchen.model.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
    Ingredient findByNameIgnoreCase(String name);
    boolean existsByNameIgnoreCase(String name);
}

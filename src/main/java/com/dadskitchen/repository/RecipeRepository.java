package com.dadskitchen.repository;

import com.dadskitchen.model.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    Recipe findByNameIgnoreCase(String name);
}

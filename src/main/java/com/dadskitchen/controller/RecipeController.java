package com.dadskitchen.controller;

import com.dadskitchen.model.Recipe;
import com.dadskitchen.repository.RecipeRepository;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recipes")
@CrossOrigin
public class RecipeController {

    private final RecipeRepository recipeRepository;

    public RecipeController(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @GetMapping
    public List<Recipe> getAllRecipes() {
        return recipeRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Recipe> getRecipeById(Long id){
        return recipeRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Recipe create(@RequestBody Recipe recipe) {
        return recipeRepository.save(recipe);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Recipe> updateRecipe(@RequestBody Long id, @RequestBody Recipe updated){
        return recipeRepository.findById(id)
                .map(existing -> {
                    existing.editName(updated.getName());
                    existing.editInstructions(updated.getInstructions());
                    existing.editCategory(updated.getCategory());
                    existing.editNotes(updated.getNotes());
                    existing.setImage(updated.getImage());
                    existing.addIngredientsToRecipe(updated.getIngredients());
                    return ResponseEntity.ok(recipeRepository.save(existing));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        recipeRepository.deleteById(id);
    }
}

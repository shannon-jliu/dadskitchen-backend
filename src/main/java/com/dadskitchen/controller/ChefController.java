package com.dadskitchen.controller;

import com.dadskitchen.model.Chef;
import com.dadskitchen.model.Ingredient;
import com.dadskitchen.model.Inventory;
import com.dadskitchen.model.Recipe;
import com.dadskitchen.model.User;
import com.dadskitchen.repository.RecipeRepository;
import com.dadskitchen.repository.ChefRepository;
import java.util.Optional;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chefs")
@CrossOrigin
public class ChefController {

    private final ChefRepository chefRepository;
    private final RecipeRepository recipeRepository;

    public ChefController(ChefRepository chefRepository, RecipeRepository recipeRepository){
        this.chefRepository = chefRepository;
        this.recipeRepository = recipeRepository;
    }

    @GetMapping
    public List<Chef> getAllChefs(){
        return chefRepository.findAll();
    }

    @PostMapping
    public Chef createChef(@RequestBody Chef chef) {
        return chefRepository.save(chef);
    }

    @GetMapping("/{id}/recipes")
    public ResponseEntity<?> getChefRecipes(@PathVariable Long id) {
        return chefRepository.findById(id)
                .map(chef -> ResponseEntity.ok(chef.getCreatedRecipes()))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/recipes")
    public ResponseEntity<?> addRecipeToChef(@PathVariable Long id, @RequestBody Recipe recipe) {
        Optional<Chef> chefOpt = chefRepository.findById(id);
        if (chefOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Chef chef = chefOpt.get();
        chef.addExistingRecipe(recipe);
        recipeRepository.save(recipe);
        chefRepository.save(chef);

        return ResponseEntity.ok(recipe);
    }

    @PostMapping("/{id}/make")
    public ResponseEntity<?> makeDish(@PathVariable Long id, @RequestBody Recipe recipe) {
        Optional<Chef> chefOpt = chefRepository.findById(id);
        if (chefOpt.isEmpty()) return ResponseEntity.notFound().build();

        Chef chef = chefOpt.get();

        boolean success = chef.makeDish(recipe);
        chefRepository.save(chef);

        if (success) {
            return ResponseEntity.ok("Dish made successfully.");
        } else {
            return ResponseEntity.badRequest().body("Not enough ingredients to make " + recipe.getName());
        }
    }

    @PostMapping("/{id}/inventory/add")
    public ResponseEntity<?> addIngredientToInventory(
            @PathVariable Long id,
            @RequestBody Ingredient ingredient) {
        return chefRepository.findById(id)
                .map(chef -> {
                    chef.getInventory().addIngredient(ingredient);
                    chefRepository.save(chef);
                    return ResponseEntity.ok("Ingredient added/updated in inventory.");
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/inventory/remove")
    public ResponseEntity<?> removeIngredientFromInventory(
            @PathVariable Long id,
            @RequestBody Ingredient ingredient) {
        return chefRepository.findById(id)
                .map(chef -> {
                    Inventory inventory = chef.getInventory();
                    boolean exists = inventory.getItems().stream()
                            .anyMatch(item -> item.getIngredient().getName().equalsIgnoreCase(ingredient.getName()));

                    if (!exists) {
                        return ResponseEntity.badRequest().body("Ingredient not found in inventory.");
                    }

                    // Remove logic: you can decide if this decrements quantity or deletes the item.
                    // For decrementing:
                    inventory.getItems().forEach(item -> {
                        if (item.getIngredient().getName().equalsIgnoreCase(ingredient.getName())) {
                            item.updateQuantity(ingredient.getQuantity());
                        }
                    });

                    chefRepository.save(chef);
                    return ResponseEntity.ok("Ingredient quantity updated.");
                })
                .orElse(ResponseEntity.notFound().build());
    }



}

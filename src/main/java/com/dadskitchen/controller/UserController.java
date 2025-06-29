package com.dadskitchen.controller;

import com.dadskitchen.model.Recipe;
import com.dadskitchen.model.User;
import com.dadskitchen.repository.RecipeRepository;
import com.dadskitchen.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin
public class UserController {

    private final UserRepository userRepository;
    private RecipeRepository recipeRepository;

    public UserController(UserRepository userRepository, RecipeRepository recipeRepository) {
        this.userRepository = userRepository;
        this.recipeRepository = recipeRepository;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        return userRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        return userRepository.save(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User updated) {
        return userRepository.findById(id)
                .map(existing -> {
                    existing.setName(updated.getName());
                    return ResponseEntity.ok(userRepository.save(existing));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        if (!userRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        userRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/favorite")
    public ResponseEntity<?> getFavoriteRecipe(@PathVariable Long id) {
        return userRepository.findById(id)
                .map(user -> {
                    Recipe favorite = user.getFavorite();
                    return favorite != null ? ResponseEntity.ok(favorite)
                            : ResponseEntity.noContent().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/ratings")
    public ResponseEntity<?> getUserRatings(@PathVariable Long id) {
        return userRepository.findById(id)
                .map(user -> ResponseEntity.ok(user.getRatings()))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/recommended")
    public ResponseEntity<?> getRecipesUserRecommended(@PathVariable Long id) {
        return userRepository.findById(id)
                .map(user -> ResponseEntity.ok(user.getRecommendationsFromSelf()))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/recommendations")
    public ResponseEntity<?> getRecommendationsForUser(@PathVariable Long id) {
        return userRepository.findById(id)
                .map(user -> ResponseEntity.ok(user.getRecommendationsFromOthers()))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/recommend/{recipeId}/to/{otherUserId}")
    public ResponseEntity<?> recommendRecipeToUser(
            @PathVariable Long id,
            @PathVariable Long recipeId,
            @PathVariable Long otherUserId
    ) {
        User recommender = userRepository.findById(id).orElse(null);
        User receiver = userRepository.findById(otherUserId).orElse(null);
        Recipe recipe = recipeRepository.findById(recipeId).orElse(null);

        if (recommender == null || receiver == null || recipe == null) {
            return ResponseEntity.notFound().build();
        }

        recommender.getRecommendationsFromSelf().add(recipe);
        receiver.getRecommendationsFromOthers().add(recipe);
        userRepository.save(recommender);
        userRepository.save(receiver);

        return ResponseEntity.ok("Recipe recommended!");
    }

}

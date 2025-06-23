package com.dadskitchen.model;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.UUID;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;
    private String name;
    private ArrayList<Rating> ratings;
    private ArrayList<Recipe> recommended;

    public User(String name) {
        this.name = name;
        this.id = UUID.randomUUID(); // unique identifier
        this.ratings = new ArrayList<>();
        this.recommended = new ArrayList<>();
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void makeRating(Recipe recipe, int stars, String comments) {
        Rating rating = new Rating(recipe, stars, comments, this);
        ratings.add(rating);
        recipe.addRating(rating);
    }

    public Recipe getFavorite() {
        return ratings.stream()
                .max(Comparator.comparingInt(Rating::getStars))
                .map(Rating::getRecipe)
                .orElse(null);
    }

    public void recommendRecipe(Recipe recipe, User otherUser) {
        otherUser.recommended.add(recipe);
    }

    public ArrayList<Rating> getRatings() {
        return ratings;
    }

    public ArrayList<Recipe> getRecommendations() {
        return recommended;
    }
}

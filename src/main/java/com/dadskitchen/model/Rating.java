package com.dadskitchen.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User reviewer;

    private int stars; // 1 - 5
    private String comments;

    public Rating(Recipe recipe, int stars, String comments, User reviewer) {
        this.recipe = recipe;
        this.stars = stars;
        this.comments = comments.trim();
        this.reviewer = reviewer;
    }

    public void updateStars(int stars) {
        this.stars = stars;
    }

    public void updateComment(String comment) {
        this.comments += "\n" + comment.trim();
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public int getStars() {
        return stars;
    }

    public String getComments() {
        return comments;
    }

    public User getReviewer() {
        return reviewer;
    }

    @Override
    public String toString() {
        return "Rating for: " + recipe.getName() +
                "\nBy: " + reviewer.getName() +
                "\nStars: " + stars +
                "\nComments: " + comments;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}

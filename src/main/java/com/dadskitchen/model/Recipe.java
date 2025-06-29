package com.dadskitchen.model;
import java.time.LocalDate;
import java.util.ArrayList;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String instructions;
    private String notes;
    private String category;
    private String imageUrl;
    private LocalDate lastMade;
    private int frequencyMade;

    @ManyToMany
    @JoinTable(
            name = "recipe_ingredients",
            joinColumns = @JoinColumn(name = "recipe_id"),
            inverseJoinColumns = @JoinColumn(name = "ingredient_id")
    )
    private ArrayList<Ingredient> ingredients;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    private ArrayList<Rating> ratings;

    public Recipe(String name, String category, ArrayList<Ingredient> ingredients, String instructions) {
        this.name = name;
        this.category = category;
        this.instructions = instructions;
        this.notes = "";
        this.imageUrl = null;
        this.lastMade = null;
        this.frequencyMade = 0;
        this.ingredients = ingredients;
        this.ratings = new ArrayList<>();
    }

    public void editName(String newName) {
        this.name = newName;
    }

    public void editInstructions(String newInstructions) {
        this.instructions = newInstructions;
    }

    public void editCategory(String newCategory){
        this.category = newCategory;
    }

    public void setImage(String newImageUrl) {
        this.imageUrl = newImageUrl;
    }

    public void addIngredientsToRecipe(ArrayList<Ingredient> ingredients){
        for (Ingredient ing : ingredients) {
            if (!this.ingredients.contains(ing)) {
                this.ingredients.add(ing);
            }
        }
    }

    public void editNotes(String newNotes) {
        this.notes = newNotes;
    }

    public void markAsMade() {
        this.lastMade = LocalDate.now();
        this.frequencyMade++;
    }

    public double getAverageStars() {
        return ratings.stream().mapToInt(Rating::getStars).average().orElse(0);
    }

    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }

    public String getName() {
        return name;
    }

    public void addRating(Rating rating) {
        this.ratings.add(rating);
    }

    @Override
    public String toString() {
        return "Recipe: " + name +
                "\nCategory: " + category +
                "\nInstructions: " + instructions +
                "\nNotes: " + notes +
                "\nLast Made: " + (lastMade != null ? lastMade : "N/A") +
                "\nTimes Made: " + frequencyMade +
                "\nAvg Rating: " + getAverageStars();
    }

    public String getInstructions() {
        return this.instructions;
    }

    public String getCategory() {
        return this.category;
    }

    public String getNotes() {
        return this.notes;
    }

    public String getImage() {
        return this.imageUrl;
    }
}

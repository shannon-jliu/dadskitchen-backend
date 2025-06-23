package com.dadskitchen.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.OneToOne;
import java.util.ArrayList;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Chef extends User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private ArrayList<Recipe> createdRecipes;

    @OneToOne(cascade = CascadeType.ALL)
    private Inventory inventory;

    public Chef(String name) {
        super(name);
        this.createdRecipes = new ArrayList<>();
        this.inventory = new Inventory();
    }

    public void createRecipe(String name, String category, ArrayList<Ingredient> ingredients, String instructions) {
        Recipe recipe = new Recipe(name, category, ingredients, instructions);
        createdRecipes.add(recipe);
    }

    public boolean makeDish(Recipe recipe) {
        if (inventory.canUseRecipe(recipe)) {
            inventory.useItemsForRecipe(recipe);
            recipe.markAsMade(); // Track last made and frequency
            return true;
        } else {
            System.out.println("Not enough ingredients to make: " + recipe.getName());
            return false;
        }
    }

    public Inventory getInventory() {
        return inventory;
    }

    public ArrayList<Recipe> getCreatedRecipes() {
        return createdRecipes;
    }
}

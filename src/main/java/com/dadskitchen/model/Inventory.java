package com.dadskitchen.model;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Entity
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "inventory", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<InventoryItem> items = new ArrayList<>();

    public Inventory() {}

    public void addIngredient(Ingredient newIng) {
        for (InventoryItem item : items) {
            if (item.getIngredient().getName().equalsIgnoreCase(newIng.getName())) {
                if (!item.getUnit().equalsIgnoreCase(newIng.getUnit())) {
                    newIng = createNewConvertedIngredient(newIng, item.getUnit());
                }
                item.updateQuantity(-newIng.getQuantity());
                return;
            }
        }

        InventoryItem newItem = new InventoryItem(newIng, newIng.getQuantity(), newIng.getUnit());
        newItem.setInventory(this); // set back-reference
        items.add(newItem);
    }

    public boolean canUseRecipe(Recipe recipe) {
        for (Ingredient ing : recipe.getIngredients()) {
            InventoryItem stock = items.stream()
                    .filter(i -> i.getIngredient().getName().equals(ing.getName()))
                    .findFirst()
                    .orElse(null);

            if (stock == null || !stock.getUnit().equals(ing.getUnit()) || stock.getQuantity() < ing.getQuantity()) {
                return false;
            }
        }
        return true;
    }

    public void useItemsForRecipe(Recipe recipe) {
        if (!canUseRecipe(recipe)) return;

        for (Ingredient ing : recipe.getIngredients()) {
            for (InventoryItem item : items) {
                if (item.getIngredient().getName().equals(ing.getName())) {
                    item.updateQuantity(ing.getQuantity());
                    break;
                }
            }
        }
    }

    public Ingredient createNewConvertedIngredient(Ingredient ingredient, String goal) {

        // cups, pints, quarts, gallons, ounces, grams, pounds

        String name = ingredient.getName();
        String unit = ingredient.getUnit();
        double quantity = ingredient.getQuantity();

        if (goal.equals("gallons")) {
            if (unit.equals("quarts"))
                return new Ingredient(name, quantity / 4.0, goal);
            if (unit.equals("pints"))
                return new Ingredient(name, quantity / 8.0, goal);
            if (unit.equals("cups"))
                return new Ingredient(name, quantity / 16.0, goal);
            if (unit.equals("ounces"))
                return new Ingredient(name, quantity / 128.0, goal);
        } else if (goal.equals("quarts")) {
            if (unit.equals("gallons"))
                return new Ingredient(name, quantity * 4.0, goal);
            if (unit.equals("pints"))
                return new Ingredient(name, quantity / 2.0, goal);
            if (unit.equals("cups"))
                return new Ingredient(name, quantity / 4.0, goal);
            if (unit.equals("ounces"))
                return new Ingredient(name, quantity / 32.0, goal);
        } else if (goal.equals("pints")) {
            if (unit.equals("gallons"))
                return new Ingredient(name, quantity * 8.0, goal);
            if (unit.equals("quarts"))
                return new Ingredient(name, quantity * 2.0, goal);
            if (unit.equals("cups"))
                return new Ingredient(name, quantity / 2.0, goal);
            if (unit.equals("ounces"))
                return new Ingredient(name, quantity / 16.0, goal);
        } else if (goal.equals("cups")) {
            if (unit.equals("gallons"))
                return new Ingredient(name, quantity * 16.0, goal);
            if (unit.equals("quarts"))
                return new Ingredient(name, quantity * 4.0, goal);
            if (unit.equals("pints"))
                return new Ingredient(name, quantity * 2.0, goal);
            if (unit.equals("ounces"))
                return new Ingredient(name, quantity / 8.0, goal);
        } else if (goal.equals("ounces")) {
            if (unit.equals("gallons"))
                return new Ingredient(name, quantity * 128.0, goal);
            if (unit.equals("quarts"))
                return new Ingredient(name, quantity * 32.0, goal);
            if (unit.equals("pints"))
                return new Ingredient(name, quantity * 16.0, goal);
            if (unit.equals("cups"))
                return new Ingredient(name, quantity * 8.0, goal);
        }

        if (goal.equals("grams")) {
            if (unit.equals("pounds"))
                return new Ingredient(name, quantity * 453.592, goal);
        } else if (goal.equals("pounds")) {
            if (unit.equals("grams"))
                return new Ingredient(name, quantity / 453.592, goal);
        }

        throw new IllegalArgumentException("Cannot convert from " + unit + " to " + goal);

    }
}

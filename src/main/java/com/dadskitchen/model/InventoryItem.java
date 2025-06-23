package com.dadskitchen.model;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import java.time.LocalDate;

@Entity
public class InventoryItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ingredient_id")
    private Ingredient ingredient;

    @ManyToOne
    @JoinColumn(name = "inventory_id")
    private Inventory inventory;

    private double quantity;
    private String unit;
    private boolean available;
    private LocalDate lastUsed;

    public InventoryItem() {}

    public InventoryItem(Ingredient ingredient, double quantity, String unit) {
        this.ingredient = ingredient;
        this.quantity = quantity;
        this.unit = unit;
        this.available = quantity > 0;
        this.lastUsed = null;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public double getQuantity() {
        return quantity;
    }

    public String getUnit() {
        return unit;
    }

    public boolean isAvailable() {
        return available;
    }

    public LocalDate getLastUsed() {
        return lastUsed;
    }

    public void updateQuantity(double used) {
        this.quantity -= used;
        this.available = this.quantity > 0;
        this.lastUsed = LocalDate.now();
    }

    @Override
    public String toString() {
        return quantity + " " + unit + " of " + ingredient.getName() + " (Available: " + available + ")";
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }
}

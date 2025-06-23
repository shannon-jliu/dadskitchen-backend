package com.dadskitchen.model;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Ingredient {
    @Id @GeneratedValue
    private Long id;
    private String name;
    private double quantity;
    private String unit;

    public Ingredient(String name, double quantity, String unit) {
        this.name = name.toLowerCase().trim();
        this.quantity = quantity;
        this.unit = unit.toLowerCase().trim();
    }

    public String getName() { return name; }
    public double getQuantity() { return quantity; }
    public String getUnit() { return unit; }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return quantity + " " + unit + " of " + name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ingredient other)) return false;
        return name.equalsIgnoreCase(other.name) && unit.equalsIgnoreCase(other.unit);
    }

    @Override
    public int hashCode() {
        return name.toLowerCase().hashCode() + unit.toLowerCase().hashCode();
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}

package com.dadskitchen.controller;

import com.dadskitchen.model.Ingredient;
import com.dadskitchen.model.Inventory;
import com.dadskitchen.model.Recipe;
import com.dadskitchen.repository.InventoryRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inventory")
@CrossOrigin
public class InventoryController {

    private final InventoryRepository inventoryRepository;

    public InventoryController(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    @PostMapping("/{inventoryId}/add")
    public ResponseEntity<?> addIngredientToInventory(@PathVariable Long inventoryId, @RequestBody Ingredient ingredient) {
        return inventoryRepository.findById(inventoryId)
                .map(inventory -> {
                    inventory.addIngredient(ingredient);
                    inventoryRepository.save(inventory);
                    return ResponseEntity.ok(inventory);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{inventoryId}/can-make")
    public ResponseEntity<?> canMakeRecipe(@PathVariable Long inventoryId, @RequestBody Recipe recipe) {
        return inventoryRepository.findById(inventoryId)
                .map(inventory -> ResponseEntity.ok(inventory.canUseRecipe(recipe)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{inventoryId}/use")
    public ResponseEntity<?> useIngredients(@PathVariable Long inventoryId, @RequestBody Recipe recipe) {
        return inventoryRepository.findById(inventoryId)
                .map(inventory -> {
                    if (!inventory.canUseRecipe(recipe)) {
                        return ResponseEntity.badRequest().body("Not enough ingredients!");
                    }
                    inventory.useItemsForRecipe(recipe);
                    inventoryRepository.save(inventory);
                    return ResponseEntity.ok(inventory);
                })
                .orElse(ResponseEntity.notFound().build());
    }

}

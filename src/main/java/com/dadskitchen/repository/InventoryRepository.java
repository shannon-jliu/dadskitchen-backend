package com.dadskitchen.repository;

import com.dadskitchen.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {


}


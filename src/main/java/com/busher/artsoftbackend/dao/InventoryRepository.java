package com.busher.artsoftbackend.dao;

import com.busher.artsoftbackend.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
}

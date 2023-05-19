package com.microdervice.inventoryservice.repository;

import com.microdervice.inventoryservice.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface InventoryRepository extends JpaRepository<Inventory , Long> {

    List<Inventory> findByskuCodeIn(List<String> skuCodeList);
}

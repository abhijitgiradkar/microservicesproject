package com.microdervice.inventoryservice.service;

import com.microdervice.inventoryservice.dto.InventoryResponse;
import com.microdervice.inventoryservice.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    @Transactional(readOnly = true)
    public List<InventoryResponse> checkInventory(List<String> skuCodeList) {
        return inventoryRepository.findByskuCodeIn(skuCodeList).stream()
                .map(inventory -> InventoryResponse.builder()
                        .skuCode(inventory.getSkuCode())
                        .isInStock(inventory.getQuantity()>0)
                        .build()).collect(Collectors.toList());
    }
}

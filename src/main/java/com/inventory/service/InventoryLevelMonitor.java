package com.inventory.service;

import com.inventory.entity.Product;
import com.inventory.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@Slf4j
public class InventoryLevelMonitor {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private NotificationService notificationService;

    // Run every hour to check stock levels
    @Scheduled(fixedRate = 3600000)
    public void monitorStockLevels() {
        log.info("Starting scheduled stock level monitoring...");
        List<Product> products = productRepository.findAll();

        for (Product product : products) {
            int currentStock = product.getInventory() != null ? product.getInventory().getCurrentStock() : 0;

            if (currentStock <= product.getReorderThreshold()) {
                log.warn("Low stock detected for product: {}. Current: {}, Threshold: {}",
                        product.getName(), currentStock, product.getReorderThreshold());

                // Automatically suggest a procurement order
                suggestProcurement(product);

                // Notify relevant roles
                notificationService.sendLowStockAlert(product, currentStock);
            }

            if (currentStock >= product.getMaxStockLevel()) {
                log.info("Overstock detected for product: {}. Current: {}, Max: {}",
                        product.getName(), currentStock, product.getMaxStockLevel());
            }
        }
    }

    private void suggestProcurement(Product product) {
        // Logic to generate a draft PO if one doesn't already exist for this
        // product/supplier
        log.info("Suggesting procurement for {}", product.getName());
        // This could call orderService.generateDraftOrder(product.getId(), quantity);
    }
}

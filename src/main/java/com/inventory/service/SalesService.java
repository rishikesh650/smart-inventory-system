package com.inventory.service;

import com.inventory.entity.Inventory;
import com.inventory.entity.Product;
import com.inventory.entity.Sale;
import com.inventory.repository.InventoryRepository;
import com.inventory.repository.ProductRepository;
import com.inventory.repository.SaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class SalesService {
    @Autowired
    private SaleRepository saleRepository;
    @Autowired
    private InventoryRepository inventoryRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private AuditLogService auditLogService;

    @Transactional
    public Sale recordSale(Long productId, int quantity) {
        Product product = productRepository.findById(productId).orElseThrow();
        Inventory inventory = inventoryRepository.findByProductId(productId).orElseThrow();

        if (inventory.getCurrentStock() < quantity) {
            throw new RuntimeException("Operational Failure: Insufficient inventory for " + product.getName()
                    + ". Available: " + inventory.getCurrentStock() + ", Requested: " + quantity);
        }

        inventory.setCurrentStock(inventory.getCurrentStock() - quantity);
        inventory.setLastUpdated(LocalDateTime.now());
        inventoryRepository.save(inventory);

        Sale sale = new Sale();
        sale.setProduct(product);
        sale.setQuantity(quantity);
        sale.setUnitPrice(product.getUnitPrice());
        sale.setTotalPrice(product.getUnitPrice().multiply(BigDecimal.valueOf(quantity)));
        sale.setSaleDate(LocalDateTime.now());

        Sale savedSale = saleRepository.save(sale);
        auditLogService.logAction("system", "SALE_RECORDED",
                String.format("Product: %s, Quantity: %d, Total: %s", product.getName(), quantity,
                        sale.getTotalPrice()));
        return savedSale;
    }

    public List<Sale> getSalesInRange(LocalDateTime start, LocalDateTime end) {
        // Simple implementation for demo
        return saleRepository.findAll();
    }
}

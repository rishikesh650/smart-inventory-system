package com.inventory.service;

import com.inventory.entity.Inventory;
import com.inventory.entity.Product;
import com.inventory.repository.InventoryRepository;
import com.inventory.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private InventoryRepository inventoryRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Transactional
    public Product saveProduct(Product product) {
        Product savedProduct = productRepository.save(product);
        if (savedProduct.getInventory() == null) {
            Inventory inventory = new Inventory();
            inventory.setProduct(savedProduct);
            inventory.setCurrentStock(0);

            inventoryRepository.save(inventory);
        }
        return savedProduct;
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id).orElseThrow();
    }
}

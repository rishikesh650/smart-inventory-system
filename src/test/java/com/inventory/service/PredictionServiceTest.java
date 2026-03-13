package com.inventory.service;

import com.inventory.dto.PredictionDTO;
import com.inventory.entity.Inventory;
import com.inventory.entity.Product;
import com.inventory.repository.ProductRepository;
import com.inventory.repository.SaleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Collections;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PredictionServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private SaleRepository saleRepository;

    @InjectMocks
    private PredictionService predictionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCriticalAlertLevel() {
        Product product = new Product();
        product.setName("Test Product");
        product.setReorderThreshold(10);
        product.setMinStockLevel(5);
        product.setLeadTimeDays(3);

        Inventory inventory = new Inventory();
        inventory.setCurrentStock(2); // Below min stock
        product.setInventory(inventory);

        when(productRepository.findAll()).thenReturn(Collections.singletonList(product));
        when(saleRepository.findAll()).thenReturn(Collections.emptyList());

        List<PredictionDTO> predictions = predictionService.getPredictions();

        assertFalse(predictions.isEmpty());
        assertEquals("CRITICAL", predictions.get(0).getAlertLevel());
    }
}

package com.inventory.service;

import com.inventory.entity.*;
import com.inventory.repository.InventoryRepository;
import com.inventory.repository.ProductRepository;
import com.inventory.repository.SaleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SalesServiceTest {

    @Mock
    private SaleRepository saleRepository;

    @Mock
    private InventoryRepository inventoryRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private AuditLogService auditLogService;

    @InjectMocks
    private SalesService salesService;

    private Product product;
    private Inventory inventory;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setId(1L);
        product.setName("Test Product");
        product.setUnitPrice(new BigDecimal("10.00"));

        inventory = new Inventory();
        inventory.setProduct(product);
        inventory.setCurrentStock(100);
    }

    @Test
    void recordSale_Success_WithPaymentDetails() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(inventoryRepository.findByProductId(1L)).thenReturn(Optional.of(inventory));
        when(saleRepository.save(any(Sale.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Sale sale = salesService.recordSale(1L, 5, PaymentMethod.UPI, PaymentStatus.COMPLETED);

        assertNotNull(sale);
        assertEquals(PaymentMethod.UPI, sale.getPaymentMethod());
        assertEquals(PaymentStatus.COMPLETED, sale.getPaymentStatus());
        assertEquals(95, inventory.getCurrentStock());
        
        verify(saleRepository).save(any(Sale.class));
        verify(auditLogService).logAction(anyString(), eq("SALE_RECORDED"), anyString());
    }

    @Test
    void recordSale_InsufficientInventory() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(inventoryRepository.findByProductId(1L)).thenReturn(Optional.of(inventory));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            salesService.recordSale(1L, 105, PaymentMethod.CASH, PaymentStatus.PENDING);
        });

        assertTrue(exception.getMessage().contains("Insufficient inventory"));
        verify(saleRepository, never()).save(any(Sale.class));
    }

    @Test
    void recordSale_NullPaymentDetails() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(inventoryRepository.findByProductId(1L)).thenReturn(Optional.of(inventory));
        when(saleRepository.save(any(Sale.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Testing the edge case where payment details might be missing (null)
        Sale sale = salesService.recordSale(1L, 5, null, null);

        assertNotNull(sale);
        assertNull(sale.getPaymentMethod());
        assertNull(sale.getPaymentStatus());
        
        verify(saleRepository).save(any(Sale.class));
    }
}

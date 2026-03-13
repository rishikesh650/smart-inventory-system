package com.inventory.service;

import com.inventory.entity.Product;
import com.inventory.entity.PurchaseOrder;
import com.inventory.entity.Supplier;
import com.inventory.entity.OrderStatus;
import com.inventory.repository.ProductRepository;
import com.inventory.repository.PurchaseOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class OrderService {
    @Autowired
    private PurchaseOrderRepository orderRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private AuditLogService auditLogService;

    public List<PurchaseOrder> getAllOrders() {
        return orderRepository.findAll();
    }

    @Transactional
    public void generateOrder(Long productId, int quantity) {
        Product product = productRepository.findById(productId).orElseThrow();
        Supplier supplier = calculateOptimalOrderSource(product);

        PurchaseOrder order = new PurchaseOrder();
        order.setSupplier(supplier);
        order.setStatus(OrderStatus.PENDING);
        order.setOrderDate(java.time.LocalDateTime.now());

        // In a real system, we'd add an item to the PO
        orderRepository.save(order);
    }

    public Supplier calculateOptimalOrderSource(Product product) {
        // In an enterprise system, this would query all suppliers for this product's
        // category
        // and compare lead times, price, and performance metrics.
        // For now, return the primary supplier or implement simple comparison logic if
        // multiple exist.
        return product.getSupplier();
    }

    @Transactional
    public PurchaseOrder approveOrder(Long orderId, String approvedBy) {
        PurchaseOrder order = orderRepository.findById(orderId).orElseThrow();
        order.setStatus(OrderStatus.APPROVED);
        order.setApprovedBy(approvedBy);
        PurchaseOrder savedOrder = orderRepository.save(order);
        auditLogService.logAction(approvedBy, "ORDER_APPROVED", "PO ID: " + orderId);
        return savedOrder;
    }

    public PurchaseOrder saveOrder(PurchaseOrder order) {
        return orderRepository.save(order);
    }
}

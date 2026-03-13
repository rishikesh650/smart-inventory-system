package com.inventory.controller;

import com.inventory.entity.PurchaseOrder;
import com.inventory.entity.Supplier;
import com.inventory.service.OrderService;
import com.inventory.service.ProductService;
import com.inventory.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductService productService;

    @Autowired
    private SupplierService supplierService;

    @GetMapping
    public String orders(Model model) {
        model.addAttribute("orders", orderService.getAllOrders());
        model.addAttribute("pageTitle", "Purchase Orders");
        return "orders";
    }

    @GetMapping("/add")
    public String addOrderForm(Model model) {
        PurchaseOrder order = new PurchaseOrder();
        order.setSupplier(new Supplier());
        model.addAttribute("order", order);
        model.addAttribute("products", productService.getAllProducts());
        model.addAttribute("suppliers", supplierService.getAllSuppliers());
        model.addAttribute("pageTitle", "Create Purchase Order");
        return "order-form";
    }

    @PostMapping("/save")
    public String saveOrder(@ModelAttribute PurchaseOrder order, BindingResult result) {
        if (result.hasErrors()) {
            return "order-form";
        }
        orderService.saveOrder(order);
        return "redirect:/admin/orders";
    }

    @PostMapping("/generate")
    public String generateOrder(@RequestParam Long productId, @RequestParam int quantity) {
        orderService.generateOrder(productId, quantity);
        return "redirect:/admin/orders";
    }
}

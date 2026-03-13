package com.inventory.controller;

import com.inventory.service.SalesService;
import com.inventory.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/sales")
public class SalesController {

    @Autowired
    private SalesService salesService;

    @Autowired
    private ProductService productService;

    @GetMapping
    public String sales(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        model.addAttribute("pageTitle", "Sales Management");
        return "sales";
    }

    @PostMapping("/record")
    public String recordSale(@RequestParam Long productId, @RequestParam int quantity) {
        salesService.recordSale(productId, quantity);
        return "redirect:/dashboard";
    }
}

package com.inventory.controller;

import com.inventory.entity.Product;
import com.inventory.service.ProductService;
import com.inventory.service.SupplierService;
import com.inventory.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private SupplierService supplierService;

    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping
    public String listProducts(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        model.addAttribute("pageTitle", "Product Inventory");
        return "products";
    }

    @GetMapping("/add")
    public String addProductForm(Model model) {
        Product product = new Product();
        model.addAttribute("product", product);
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("suppliers", supplierService.getAllSuppliers());
        model.addAttribute("pageTitle", "Add New Product");
        return "product-form";
    }

    @GetMapping("/edit/{id}")
    public String editProductForm(@PathVariable Long id, Model model) {
        model.addAttribute("product", productService.getProductById(id));
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("suppliers", supplierService.getAllSuppliers());
        model.addAttribute("pageTitle", "Edit Product");
        return "product-form";
    }

    @PostMapping("/save")
    public String saveProduct(@ModelAttribute Product product, BindingResult result) {
        if (result.hasErrors()) {
            return "product-form";
        }
        productService.saveProduct(product);
        return "redirect:/products";
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        // Logic for deletion (could be soft delete for enterprise)
        // For now, let's just implement a simple delete if needed,
        // but the prompt didn't explicitly ask for it.
        // I'll stick to the existing functionality for now and enhance as I go.
        return "redirect:/products";
    }
}

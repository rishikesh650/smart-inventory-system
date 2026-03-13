package com.inventory.controller;

import com.inventory.entity.Supplier;
import com.inventory.service.SupplierService;
import com.inventory.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/suppliers")
public class SupplierController {

    @Autowired
    private SupplierService supplierService;

    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping
    public String suppliers(Model model) {
        model.addAttribute("suppliers", supplierService.getAllSuppliers());
        model.addAttribute("pageTitle", "Supplier Management");
        return "suppliers";
    }

    @GetMapping("/add")
    public String addSupplierForm(Model model) {
        Supplier supplier = new Supplier();
        model.addAttribute("supplier", supplier);
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("pageTitle", "Add New Supplier");
        return "supplier-form";
    }

    @PostMapping("/save")
    public String saveSupplier(@ModelAttribute Supplier supplier, BindingResult result) {
        if (result.hasErrors()) {
            return "supplier-form";
        }
        supplierService.saveSupplier(supplier);
        return "redirect:/suppliers";
    }
}

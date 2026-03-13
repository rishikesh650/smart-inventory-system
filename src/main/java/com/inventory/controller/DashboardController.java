package com.inventory.controller;

import com.inventory.dto.PredictionDTO;
import com.inventory.dto.UserDTO;
import com.inventory.service.PredictionService;
import com.inventory.service.ProductService;
import com.inventory.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.List;

@Controller
public class DashboardController {

    public DashboardController() {
        System.out.println("DEBUG: DashboardController Bean Created!");
    }

    @GetMapping("/debug/ping")
    @ResponseBody
    public String ping() {
        return "PONG - DashboardController is active";
    }

    @Autowired
    private ProductService productService;

    @Autowired
    private PredictionService predictionService;

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String index() {
        return "redirect:/dashboard";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        System.out.println("DEBUG: showRegistrationForm() CALLED");
        model.addAttribute("user", new UserDTO());
        return "register";
    }

    @PostMapping("/register")
    public String processRegistration(@ModelAttribute("user") UserDTO userDTO,
            BindingResult result,
            Model model) {
        System.out.println(
                "DEBUG: processRegistration() CALLED for: " + (userDTO != null ? userDTO.getUsername() : "NULL"));

        if (result.hasErrors()) {
            System.err.println("DEBUG: Validation errors: " + result.getAllErrors());
            return "register";
        }

        try {
            userService.registerNewUser(userDTO);
            System.out.println("DEBUG: Registration success!");
            return "redirect:/login?registered";
        } catch (Exception e) {
            System.err.println("DEBUG: Registration error: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", e.getMessage());
            return "register";
        }
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        try {
            List<PredictionDTO> predictions = predictionService.getPredictions();
            model.addAttribute("predictions", predictions);
            model.addAttribute("totalProducts", productService.getAllProducts().size());

            long lowStockCount = predictions.stream()
                    .filter(p -> p.getEstimatedDaysLeft() < 7)
                    .count();
            model.addAttribute("lowStockAlerts", lowStockCount);
            model.addAttribute("pageTitle", "Dashboard Overview");
            return "dashboard";
        } catch (Exception e) {
            throw e;
        }
    }
}

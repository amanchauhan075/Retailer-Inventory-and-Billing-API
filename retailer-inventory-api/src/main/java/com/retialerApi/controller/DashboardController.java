package com.retialerApi.controller;

import com.retialerApi.entity.Product;
import com.retialerApi.entity.Retailer;
import com.retialerApi.repository.OrderItemRepository;
import com.retialerApi.repository.ProductRepository;
import com.retialerApi.repository.RetailerRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;

@Controller
public class DashboardController {

    @Autowired
    private ProductRepository productRepo;

    @Autowired
    private OrderItemRepository orderItemRepo;
    
    @Autowired
    private RetailerRepository retailerRepo;

    @GetMapping("/dashboard")
    public String showDashboard(Model model, Principal principal) {
        String username = principal.getName();
        Retailer retailer = retailerRepo.findByUsername(username); // ✅ fetch Retailer
        String realName = retailer.getName(); // ✅ real name

        List<Product> lowStockProducts = productRepo.findByStockLessThanAndRetailerUsername(5, username);
        model.addAttribute("lowStockProducts", lowStockProducts);

        model.addAttribute("username", username);
        model.addAttribute("retailerName", realName);

        return "dashboard";
    }

}

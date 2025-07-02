package com.retialerApi.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.retialerApi.entity.Retailer;
import com.retialerApi.repository.RetailerRepository;

import org.springframework.ui.Model;

@Controller
public class AuthController {

    @Autowired
    private RetailerRepository retailerRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("retailer", new Retailer());
        return "register";
    }

    @PostMapping("/do_register")
    public String registerRetailer(@ModelAttribute Retailer retailer) {
        retailer.setPassword(passwordEncoder.encode(retailer.getPassword()));
        retailerRepository.save(retailer);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }
}

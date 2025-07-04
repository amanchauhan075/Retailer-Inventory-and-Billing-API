package com.retialerApi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class PaymentController {

    @GetMapping("/payment")
    public String showPaymentPage(@RequestParam("amount") double amount, Model model) {
        model.addAttribute("amount", amount);
        return "payment"; // points to payment.html
    }

    @PostMapping("/payment/process")
    public String processFakePayment(@RequestParam("amount") double amount, Model model) {
        // You can update order status here or just simulate payment success
        model.addAttribute("message", "✅ Payment of ₹" + amount + " successful!");
        return "payment-success";
    }
}

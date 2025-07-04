package com.retialerApi.controller;

import com.retialerApi.entity.OrderItem;
import com.retialerApi.entity.Product;
import com.retialerApi.entity.Retailer;
import com.retialerApi.entity.orders;
import com.retialerApi.repository.OrderRepository;
import com.retialerApi.repository.ProductRepository;
import com.retialerApi.repository.RetailerRepository;
import com.retialerApi.service.InvoiceService;
import org.springframework.data.domain.Sort.Order;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
 // 👈 ADD THIS

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderRepository orderRepo;

    @Autowired
    private ProductRepository productRepo;

    @Autowired
    private InvoiceService invoiceService;
    
    @Autowired
    private RetailerRepository retailerRepository;
    
    
    @GetMapping
    public String viewOrderHistory(Model model, Principal principal) {
        // 1. Get current username
        String username = principal.getName();

        // 2. Find retailer object using username
        Retailer retailer = retailerRepository.findByUsername(username);

        // 3. Fetch only that retailer's orders
        List<orders> orderList = orderRepo.findByRetailerUsername(retailer.getUsername());

        // 4. Add to model
        model.addAttribute("orders", orderList);
        return "order-history";
    }
    // ✅ View all orders
//    @GetMapping
//    public String viewOrderHistory(Model model) {
//        model.addAttribute("orders", orderRepo.findAll());
//        return "order-history";
//    }

    // ✅ Show offline order form
    @GetMapping("/offline")
    public String showOfflineForm() {
        return "place-order";
    }
    
    
    
    @PostMapping("/offline")
    @Transactional
    public String placeOfflineOrder(
            @RequestParam("customerName") String customerName,
            @RequestParam("productId") List<Long> productIds,
            @RequestParam("quantity") List<Integer> quantities,
            Model model,
            Principal principal) {

        // 1. Get logged in retailer
        String username = principal.getName();
        Retailer retailer = retailerRepository.findByUsername(username);

        if (retailer == null) {
            throw new RuntimeException("Retailer not found.");
        }

        orders order = new orders();
        order.setCustomerName(customerName);
        order.setOrderDate(LocalDateTime.now());
        order.setRetailerUsername(username);

        List<OrderItem> items = new ArrayList<>();
        double total = 0;

        for (int i = 0; i < productIds.size(); i++) {
            Long productId = productIds.get(i);
            int qty = quantities.get(i);

            Product product = productRepo.findById(productId)
                    .orElseThrow(() -> new RuntimeException("Product not found."));

            // ✅ CHECK: Ensure product belongs to the logged-in retailer
            if (!product.getRetailer().getId().equals(retailer.getId())) {
                throw new RuntimeException("You cannot order a product that doesn't belong to you.");
            }

            // ✅ Check stock
            if (qty <= 0 || product.getStock() < qty) {
                throw new RuntimeException("Invalid quantity or not enough stock.");
            }

            // ✅ Deduct stock
            product.setStock(product.getStock() - qty);
            productRepo.save(product);

            // ✅ Create OrderItem
            OrderItem item = new OrderItem();
            item.setProductName(product.getName());
            item.setPrice(product.getPrice());
            item.setQuantity(qty);
            item.setTotalPrice(product.getPrice() * qty);
            item.setOrder(order);
            item.setCustomerName(customerName);

            total += item.getTotalPrice();
            items.add(item);
        }

        order.setItems(items);
        orders savedOrder = orderRepo.save(order);

        // Re-link saved order to items (optional if cascade is set)
        for (OrderItem item : items) {
            item.setOrder(savedOrder);
        }

        model.addAttribute("orderId", savedOrder.getId());
        model.addAttribute("total", total);

        return "order-success";
    }



    // ✅ Place order and update stock
 

//    @PostMapping("/offline")
//    @Transactional
//    public String placeOfflineOrder(@RequestParam("customerName") String customerName,
//                                    @RequestParam("productId") List<Long> productIds,
//                                    @RequestParam("quantity") List<Integer> quantities,
//                                    Model model, Principal principal) {
//    	
//        orders order = new orders();
//        order.setCustomerName(customerName); // 👈 Save entered customer name
//        order.setOrderDate(LocalDateTime.now());
//
//        List<OrderItem> items = new ArrayList<>();
//        double total = 0;
//
//        for (int i = 0; i < productIds.size(); i++) {
//            Long productId = productIds.get(i);
//            int qty = quantities.get(i);
//
//            Product product = productRepo.findById(productId)
//                    .orElseThrow(() -> new RuntimeException("Product not found"));
//
//            if (qty <= 0 || product.getStock() < qty) {
//                throw new RuntimeException("Invalid quantity or not enough stock.");
//            }
//
//            product.setStock(product.getStock() - qty);
//            productRepo.saveAndFlush(product);
//
//            OrderItem item = new OrderItem();
//            item.setProductName(product.getName());
//            item.setPrice(product.getPrice());
//            item.setQuantity(qty);
//            item.setTotalPrice(product.getPrice() * qty);
//            item.setOrder(order);
//
//            total += item.getTotalPrice();
//            items.add(item);
//        }
//
//        order.setItems(items);
//        order.setRetailerUsername(principal.getName()); // ✅ Link retailer
//        orders savedOrder = orderRepo.save(order);
//
//        for (OrderItem item : items) {
//            item.setOrder(savedOrder); // re-link
//        }
//
//        model.addAttribute("orderId", savedOrder.getId());
//        model.addAttribute("total", total);
//
//        return "order-success";
//    }


    // ✅ Download invoice PDF
    @GetMapping("/invoice/{id}")
    public void generateInvoice(@PathVariable Long id, HttpServletResponse response) throws IOException {
        orders order = orderRepo.findById(id).orElseThrow();
        ByteArrayInputStream bis = invoiceService.generateInvoice(order);

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=invoice_order_" + id + ".pdf");
        IOUtils.copy(bis, response.getOutputStream());
        response.getOutputStream().flush();
    }
    
    @GetMapping("/product/{id}")
    @ResponseBody
    public ResponseEntity<?> getProductDetails(@PathVariable Long id, Principal principal) {
        String username = principal.getName();
        Retailer retailer = retailerRepository.findByUsername(username);

        Product product = productRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // 🔐 Check if product belongs to the logged-in retailer
        if (!product.getRetailer().getId().equals(retailer.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("You are not allowed to access this product.");
        }

        return ResponseEntity.ok(product);
    }

    // ✅ For dynamic fetch from frontend (if needed)
//    @GetMapping("/product/{id}")
//    @ResponseBody
//    public Product getProductDetails(@PathVariable Long id) {
//        return productRepo.findById(id)
//                .orElseThrow(() -> new RuntimeException("Product not found"));
//    }
    @GetMapping("/filter")
    public String filterOrdersByDate(@RequestParam("start") String start,
                                     @RequestParam("end") String end,
                                     Model model, Principal principal) {
    	try {
            // Convert LocalDate -> LocalDateTime full-day range
            LocalDateTime startDateTime = LocalDate.parse(start).atStartOfDay();
            LocalDateTime endDateTime = LocalDate.parse(end).atTime(23, 59, 59);

            String retailerUsername = principal.getName();

            List<orders> filteredOrders = orderRepo.findByRetailerUsernameAndOrderDateBetween(retailerUsername, startDateTime, endDateTime);

            model.addAttribute("orders", filteredOrders);
        } catch (Exception e) {
            model.addAttribute("error", "Invalid date format or internal error.");
            model.addAttribute("orders", new ArrayList<>());
        }

        return "order-history";
    }
    @GetMapping("/payment")
    public String showPaymentPage(@RequestParam("amount") double amount, Model model) {
        model.addAttribute("amount", amount);
        return "payment"; // It will load payment.html from templates
    }



}

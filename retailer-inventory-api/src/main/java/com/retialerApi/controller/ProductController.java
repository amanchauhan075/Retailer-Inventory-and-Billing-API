package com.retialerApi.controller;

import com.retialerApi.entity.Product;
import com.retialerApi.entity.Retailer;
import com.retialerApi.repository.ProductRepository;
import com.retialerApi.repository.RetailerRepository;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductRepository productRepo;

    @Autowired
    private RetailerRepository retailerRepo;

    // ✅ List products for logged-in retailer
    @GetMapping
    public String listProducts(Model model, Principal principal) {
        String username = principal.getName();
        List<Product> products = productRepo.findByRetailerUsername(username);
        model.addAttribute("products", products);
        return "products";
    }

    // ✅ Show Add Product Form
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("product", new Product());
        return "product_form";
    }

    // ✅ Save product with current retailer
    @PostMapping("/save")
    public String saveProduct(@ModelAttribute Product product, Principal principal, RedirectAttributes redirectAttributes) {
        String username = principal.getName();
        Retailer retailer = retailerRepo.findByUsername(username);
        product.setRetailer(retailer);

        System.out.println("✅ Form Stock Value: " + product.getStock());

        productRepo.save(product); // ✅ Save updated product
        redirectAttributes.addFlashAttribute("message", "✅ Product saved successfully!");
        return "redirect:/products";
    }

    // ✅ Show edit form
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Product product = productRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Product ID: " + id));
        model.addAttribute("product", product);
        return "product_form";
    }

    // ✅ Delete product
    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        productRepo.deleteById(id);
        redirectAttributes.addFlashAttribute("message", "🗑️ Product deleted successfully!");
        return "redirect:/products";
    }

    // ✅ Search only within logged-in retailer's products
    @GetMapping("/search")
    public String searchProducts(@RequestParam("keyword") String keyword, Model model, Principal principal) {
        String username = principal.getName();
        List<Product> result = productRepo.findByNameContainingIgnoreCaseAndRetailerUsername(keyword, username);
        model.addAttribute("products", result);
        model.addAttribute("keyword", keyword);
        return "products";
    }

    // ✅ Show stock alerts for low-stock products
    @GetMapping("/stock-alerts")
    public String showStockAlerts(Model model, Principal principal) {
        String username = principal.getName();
        List<Product> alerts = productRepo.findByStockLessThanAndRetailerUsername(5, username);
        model.addAttribute("products", alerts);
        return "stock-alerts";
    }
}

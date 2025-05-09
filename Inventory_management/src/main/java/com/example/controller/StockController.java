package com.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Entity.Discount;
import com.example.Entity.Product;
import com.example.service.StockService;

@RestController
@RequestMapping("/products")
public class StockController {

	@Autowired
	private StockService stockService;

	// Add new product
	@PostMapping
	public String addProduct(@RequestBody Product product) {
		return stockService.addProduct(product);
	}

	// Update existing product
	@PutMapping("/{id}")
	public boolean updateProduct(@PathVariable Long id, @RequestBody Product product) {
		return stockService.updateProduct(id, product);
	}

	// Get all products
	@GetMapping
	public List<Product> getAllProducts() {
		return stockService.getAllProducts();
	}

	// Get products that are low in stock
	@GetMapping("/low-stock")
	public List<Product> getLowStockProducts() {
		return stockService.getLowStockProducts();
	}

	// Generate sales report
	@GetMapping("/sales-report")
	public String generateSalesReport() {
		return stockService.generateSalesReport();
	}

	// Generate restock alert for low stock products
	@GetMapping("/restock-alert")
	public String generateRestockAlert() {
		return stockService.generateRestockAlert();
	}

	// Add a new discount
	@PostMapping("/discounts")
	public Discount addDiscount(@RequestBody Discount discount) {
		return stockService.addDiscount(discount);
	}

	// Get active discounts
	@GetMapping("/discounts")
	public List<Discount> getActiveDiscounts() {
		return stockService.getActiveDiscounts();
	}

}

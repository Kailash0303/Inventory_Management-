package com.example.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.Entity.Discount;
import com.example.Entity.Product;
import com.example.dao.StockDao;

@Service
public class StockService {

	@Autowired
	StockDao dao;

	// Add a new Product (Transaction will be automatically managed)
	@Transactional
	public String addProduct(Product product) {
		product.setDateOfCreation(LocalDateTime.now());
		product.setDateOfModification(LocalDateTime.now());

		boolean savedProduct = dao.save(product); // Save the product and capture the returned object

		if (savedProduct) { // Check if the product was saved successfully
			return "Product added successfully...";
		} else {
			return "Error: Product could not be added.";
		}
	}

	// Update an existing Product (Transaction management)
	@Transactional
	public boolean updateProduct(Long id, Product updatedProduct) {
		Optional<Product> optional = dao.findById(id.intValue());
		if (optional.isPresent()) {
			Product existing = optional.get();
			existing.setPname(updatedProduct.getPname());
			existing.setPrice(updatedProduct.getPrice());
			existing.setStock(updatedProduct.getStock());
			existing.setCategory(updatedProduct.getCategory());
			existing.setDiscountEligible(updatedProduct.isDiscountEligible());
			existing.setDateOfModification(LocalDateTime.now());
			return dao.save(existing);
		}
		throw new RuntimeException("Product not found with ID: " + id);
	}

	// Get all Products
	@Transactional(readOnly = true)
	public List<Product> getAllProducts() {
		return dao.findAll();
	}

	// Get low stock Products (Less than 20 units)
	@Transactional(readOnly = true)
	public List<Product> getLowStockProducts() {
		return dao.findByStockLessThan(20);
	}

	// Generate a Sales Report (Mocked for simplicity)
	@Transactional(readOnly = true)
	public String generateSalesReport() {
		return "Sales Report generated on " + LocalDate.now();
	}

	// Generate a Restock Alert (Mocked for simplicity)
	@Transactional(readOnly = true)
	public String generateRestockAlert() {
		List<Product> lowStock = dao.findByStockLessThan(20);
		StringBuilder alert = new StringBuilder("Restock Alerts:\n");
		for (Product p : lowStock) {
			if (p.getStock() < 10) {
				alert.append("Product ").append(p.getPname()).append(" is OUT OF STOCK!\n");
			} else {
				alert.append("Product ").append(p.getPname()).append(" needs restocking.\n");
			}
		}
		return alert.toString();
	}

	// Add a new Discount (Transaction management)
	@Transactional
	public Discount addDiscount(Discount discount) {
		discount.setDateOfCreation(LocalDateTime.now());
		discount.setDateOfModification(LocalDateTime.now());
		discount.setActive(true);
		return dao.save(discount);
	}

	// Get all Active Discounts
	@Transactional(readOnly = true)
	public List<Discount> getActiveDiscounts() {
		return dao.findByIsActiveTrue();
	}

	// Dynamic Discount Activation (based on current date)
	@Transactional
	public void updateDiscountActivation() {
		LocalDate today = LocalDate.now();
		List<Discount> discounts = dao.findAllDiscounts();

		for (Discount d : discounts) {
			boolean shouldBeActive = (today.isEqual(d.getValidFrom()) || today.isAfter(d.getValidFrom()))
					&& (today.isEqual(d.getValidTo()) || today.isBefore(d.getValidTo()));

			d.setActive(shouldBeActive);
			dao.save(d); // This should trigger the update in the database
		}
	}

	// Dynamic Pricing (based on stock level)
	@Transactional
	public void applyStockBasedPricing() {
		List<Product> products = dao.findAll();
		for (Product p : products) {
			double originalPrice = p.getPrice();

			if (p.getStock() > 1000) {
				p.setPrice(originalPrice * 0.95); // reduce 5%
			} else if (p.getStock() < 50) {
				p.setPrice(originalPrice * 1.05); // increase 5%
			}

			dao.save(p); // Save the updated product
		}
	}

	// Apply highest applicable discount (not exceeding 30%)
	@Transactional(readOnly = true)
	public double calculateBestDiscount(int productId, int orderQty, boolean isSeason) {
		double bulkDiscount = orderQty > 100 ? 0.10 : 0.0;
		double seasonalDiscount = isSeason ? 0.15 : 0.05;
		double highDemandDiscount = 0.10; // Assume it’s a high-demand product
		double bestDiscount = Math.max(bulkDiscount, Math.max(seasonalDiscount, highDemandDiscount));

		return Math.min(bestDiscount, 0.30);
	}
}

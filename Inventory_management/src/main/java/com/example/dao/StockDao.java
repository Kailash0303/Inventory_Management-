package com.example.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.Entity.Discount;
import com.example.Entity.Product;

@Repository
public class StockDao {

    @PersistenceContext
    private EntityManager entityManager;  // Inject EntityManager to handle JPA operations

    // Save or Update Product
    @Transactional
	public boolean save(Product product) {
        if (product.getPid() == 0) {
            entityManager.persist(product); // for new entities
        } else {
            entityManager.merge(product);  // for existing entities
        }
        return true;
    }

    // Save or Update Discount
    @Transactional
    public Discount save(Discount discount) {
        if (discount.getId() == 0) {
            entityManager.persist(discount); // for new entities
        } else {
            entityManager.merge(discount);  // for existing entities
        }
        return discount;
    }

    // Find Product by ID
    @Transactional
    public Optional<Product> findById(int id) {
        Product product = entityManager.find(Product.class, id);
        return Optional.ofNullable(product);
    }

    // Get all Products
    @Transactional
    public List<Product> findAll() {
        return entityManager.createQuery("SELECT p FROM Product p", Product.class).getResultList();
    }

    // Get all Discounts
    @Transactional
    public List<Discount> findAllDiscounts() {
        return entityManager.createQuery("SELECT d FROM Discount d", Discount.class).getResultList();
    }

    // Get products with stock less than a threshold
    @Transactional
    public List<Product> findByStockLessThan(int threshold) {
        return entityManager.createQuery("SELECT p FROM Product p WHERE p.stock < :threshold", Product.class)
                            .setParameter("threshold", threshold)
                            .getResultList();
    }

    // Get active discounts
    @Transactional
    public List<Discount> findByIsActiveTrue() {
        return entityManager.createQuery("SELECT d FROM Discount d WHERE d.isActive = true", Discount.class).getResultList();
    }

    // Count total Products
    @Transactional
    public Long countTotalProducts() {
        return (Long) entityManager.createQuery("SELECT COUNT(p) FROM Product p").getSingleResult();
    }

    // Get Product Names and Prices
    @Transactional
    public List<Object[]> getProductNamesAndPrices() {
        return entityManager.createQuery("SELECT p.pname, p.price FROM Product p").getResultList();
    }
}

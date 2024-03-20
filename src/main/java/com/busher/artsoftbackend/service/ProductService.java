package com.busher.artsoftbackend.service;

import com.busher.artsoftbackend.dao.InventoryRepository;
import com.busher.artsoftbackend.dao.ProductRepository;
import com.busher.artsoftbackend.model.Inventory;
import com.busher.artsoftbackend.model.Product;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;

    public ProductService(ProductRepository productRepository, InventoryRepository inventoryRepository) {
        this.productRepository = productRepository;
        this.inventoryRepository = inventoryRepository;
    }

    public Page<Product> getProducts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return productRepository.findAll(pageable);
    }

    public Page<Product> getProductsBySearchTerm(String searchTerm, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return productRepository.findAll(getSearchSpecification(searchTerm), pageable);
    }

    private Specification<Product> getSearchSpecification(String searchTerm) {
        return (root, query, criteriaBuilder) -> {
            if (searchTerm == null || searchTerm.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            Predicate namePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("name")),
                    "%" + searchTerm.toLowerCase() + "%");
            Predicate shortDescriptionPredicate = criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("shortDescription")), "%" + searchTerm.toLowerCase() + "%");
            Predicate longDescriptionPredicate = criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("longDescription")), "%" + searchTerm.toLowerCase() + "%");

            return criteriaBuilder.or(namePredicate, shortDescriptionPredicate, longDescriptionPredicate);
        };
    }

    public Optional<Inventory> findInventoryByProductId(Long productId) {
        return inventoryRepository.findById(productId);
    }

    public Optional<Product> getProduct(Long id) {
        return productRepository.findById(id);
    }

    public List<Product> getProductsByIds(Set<Long> productIds) {
        return productRepository.findAllById(productIds);
    }
}
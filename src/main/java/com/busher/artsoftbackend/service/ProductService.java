package com.busher.artsoftbackend.service;

import com.busher.artsoftbackend.dao.ProductRepository;
import com.busher.artsoftbackend.model.Product;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    public List<Product> getProducts() {
        return repository.findAll();
    }

}
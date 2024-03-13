package com.busher.artsoftbackend.api.controller;


import com.busher.artsoftbackend.model.Inventory;
import com.busher.artsoftbackend.model.Product;
import com.busher.artsoftbackend.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/product")
    public ResponseEntity<Page<Product>> getProducts(
            @RequestParam(required = false) String searchTerm,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<Product> products;
        if (searchTerm == null || searchTerm.isEmpty()) {
            products = productService.getProducts(page, size);
        } else {
            products = productService.getProductsBySearchTerm(searchTerm, page, size);
        }
        return ResponseEntity.ok(products);
    }

    @GetMapping("product/{id}/inventory")
    public ResponseEntity<Inventory> getProductInventory(@PathVariable Long id) {
        Optional<Inventory> opInventory = productService.findInventoryByProductId(id);
        return opInventory.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("product/{id}")
    public ResponseEntity<Product> getProductDetails(@PathVariable Long id) {//todo: Write tests
        Optional<Product> optionalProduct = productService.getProduct(id);
        return optionalProduct.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

}
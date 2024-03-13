package com.busher.artsoftbackend.api.controller;

import com.busher.artsoftbackend.model.Inventory;
import com.busher.artsoftbackend.model.Product;
import com.busher.artsoftbackend.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @BeforeEach
    void setUp() {
        Product product1 = new Product();
        product1.setName("Dog Toy");
        product1.setShortDescription("A durable toy for dogs");
        product1.setLongDescription("This dog toy is perfect for playtime, ensuring hours of fun.");
        product1.setPrice(15.99);

        Product product2 = new Product();
        product2.setName("Cat Scratcher");
        product2.setShortDescription("A cat scratcher that lasts");
        product2.setLongDescription("Made from durable materials, this cat scratcher will keep your pet entertained.");
        product2.setPrice(20.99);

        List<Product> allProducts = Arrays.asList(product1, product2);
        Page<Product> allProductsPage = new PageImpl<>(allProducts, PageRequest.of(0, 10), allProducts.size());

        Mockito.when(productService.getProducts(Mockito.anyInt(), Mockito.anyInt())).thenReturn(allProductsPage);

        List<Product> filteredProducts = List.of(product1);
        Page<Product> filteredProductsPage = new PageImpl<>(filteredProducts, PageRequest.of(0, 10),
                filteredProducts.size());

        Mockito.when(productService.getProductsBySearchTerm(Mockito.anyString(), Mockito.anyInt(),
                Mockito.anyInt())).thenReturn(filteredProductsPage);

        Inventory inventory = new Inventory();
        inventory.setId(1L);
        inventory.setQuantity(100);

        Mockito.when(productService.findInventoryByProductId(1L)).thenReturn(Optional.of(inventory));
        Mockito.when(productService.findInventoryByProductId(2L)).thenReturn(Optional.empty());
    }


    @Test
    void whenGetProductsWithoutSearchTerm_thenReturnsAllProducts() throws Exception {
        mockMvc.perform(get("/product")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.totalElements").value(2));
    }

    @Test
    void whenGetProductsWithSearchTerm_thenReturnsFilteredProduct() throws Exception {
        mockMvc.perform(get("/product")
                        .param("searchTerm", "dog")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].name").value("Dog Toy"))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void whenGetInventoryByProductId_andInventoryExists_thenReturnsInventory() throws Exception {
        mockMvc.perform(get("/product/1/inventory"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.quantity").value(100));
    }

    @Test
    void whenGetInventoryByProductId_andInventoryDoesNotExist_thenReturnsNotFound() throws Exception {
        mockMvc.perform(get("/product/2/inventory"))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenGetProductDetails_andProductExists_thenReturnsProduct() throws Exception {
        Long productId = 1L;
        Product product = new Product();
        product.setId(productId);
        product.setName("Dog Toy");
        product.setShortDescription("A toy for dogs");
        product.setLongDescription("Ensuring hours of fun.");
        product.setPrice(15.99);

        Mockito.when(productService.getProduct(productId)).thenReturn(Optional.of(product));

        mockMvc.perform(get("/product/{id}", productId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(productId))
                .andExpect(jsonPath("$.name").value("Dog Toy"))
                .andExpect(jsonPath("$.shortDescription").value("A toy for dogs"))
                .andExpect(jsonPath("$.longDescription").value("Ensuring hours of fun."))
                .andExpect(jsonPath("$.price").value(15.99));
    }

    @Test
    void whenGetProductDetails_andProductDoesNotExist_thenReturnsNotFound() throws Exception {
        Long productId = 3L;

        Mockito.when(productService.getProduct(productId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/product/{id}", productId))
                .andExpect(status().isNotFound());
    }

}

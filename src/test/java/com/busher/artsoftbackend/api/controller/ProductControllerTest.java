package com.busher.artsoftbackend.api.controller;

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
}

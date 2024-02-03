package com.busher.artsoftbackend.api.controller;

import com.busher.artsoftbackend.model.*;
import com.busher.artsoftbackend.service.WebOrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WebOrderService orderService;

    private LocalUser testUser;
    private List<WebOrder> testOrders;

    @BeforeEach
    public void setUp() {
        testUser = new LocalUser();
        testUser.setId(1L);
        testUser.setUsername("testUser");
        testUser.setPassword("securePassword");
        testUser.setEmail("testuser@example.com");
        testUser.setFirstName("Test");
        testUser.setLastName("User");
        testUser.setIsEmailVerified(true);

        Address address1 = new Address();
        address1.setId(201L);
        address1.setAddressLine1("123 Test");
        address1.setAddressLine2("101");
        address1.setCity("Testcity");
        address1.setCountry("Testland");
        address1.setUser(testUser);

        Address address2 = new Address();
        address2.setId(202L);
        address2.setAddressLine1("456 Demo");
        address2.setAddressLine2("202");
        address2.setCity("Democity");
        address2.setCountry("Demoland");
        address2.setUser(testUser);

        Product product1 = new Product();
        product1.setId(301L);
        product1.setName("Product 1");
        product1.setShortDescription("description of product 1");
        product1.setPrice(99.99);

        Product product2 = new Product();
        product2.setId(302L);
        product2.setName("Product 2");
        product2.setShortDescription("description of product 2");
        product2.setPrice(149.99);

        WebOrderQuantities quantities1 = new WebOrderQuantities();
        quantities1.setId(401L);
        quantities1.setProduct(product1);
        quantities1.setQuantity(2);

        WebOrderQuantities quantities2 = new WebOrderQuantities();
        quantities2.setId(402L);
        quantities2.setProduct(product2);
        quantities2.setQuantity(3);

        WebOrder order1 = new WebOrder();
        order1.setId(101L);
        order1.setUser(testUser);
        order1.setAddress(address1);
        order1.setQuantities(new ArrayList<>(List.of(quantities1)));
        quantities1.setOrder(order1);

        WebOrder order2 = new WebOrder();
        order2.setId(102L);
        order2.setUser(testUser);
        order2.setAddress(address2);
        order2.setQuantities(new ArrayList<>(List.of(quantities2)));
        quantities2.setOrder(order2);

        testOrders = Arrays.asList(order1, order2);
    }

    @Test
    public void getOrders_ShouldReturnOrdersForAuthenticatedUser() throws Exception {
        when(orderService.getWebOrders(any(LocalUser.class))).thenReturn(testOrders);

        mockMvc.perform(get("/order").with(user(testUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(testOrders.size()));
    }

    @Test
    public void getOrders_WhenNoOrders_ShouldReturnEmptyList() throws Exception {
        when(orderService.getWebOrders(any(LocalUser.class))).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/order").with(user(testUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }

}
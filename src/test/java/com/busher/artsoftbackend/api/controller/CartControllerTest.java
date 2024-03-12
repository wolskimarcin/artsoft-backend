package com.busher.artsoftbackend.api.controller;

import com.busher.artsoftbackend.api.dto.CartItemRequest;
import com.busher.artsoftbackend.model.Cart;
import com.busher.artsoftbackend.model.CartItem;
import com.busher.artsoftbackend.model.LocalUser;
import com.busher.artsoftbackend.service.CartService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CartService cartService;
    private AutoCloseable autoCloseable;
    private LocalUser testUser;

    @BeforeEach
    public void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);

        testUser = new LocalUser();
        testUser.setId(1L);
        testUser.setUsername("testUser");
        testUser.setPassword("securePassword");
        testUser.setEmail("testuser@example.com");
        testUser.setFirstName("Test");
        testUser.setLastName("User");
        testUser.setIsEmailVerified(true);
    }

    @AfterEach
    public void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void whenAddProductToCart_thenReturnCartItem() throws Exception {
        Long cartId = 1L;
        CartItemRequest cartItemRequest = new CartItemRequest(1L, 5);
        CartItem cartItem = new CartItem();
        cartItem.setProductId(cartItemRequest.getProductId());
        cartItem.setQuantity(cartItemRequest.getQuantity());

        when(cartService.addProductToCart(eq(testUser),
                eq(cartItemRequest.getProductId()),
                eq(cartItemRequest.getQuantity()))).thenReturn(cartItem);

        mockMvc.perform(post("/cart/add", cartId).with(user(testUser))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"productId\":1,\"quantity\":5}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId").value(cartItemRequest.getProductId()))
                .andExpect(jsonPath("$.quantity").value(cartItemRequest.getQuantity()));

        verify(cartService, times(1)).addProductToCart(eq(testUser), eq(cartItemRequest.getProductId()),
                eq(cartItemRequest.getQuantity()));
    }

    @Test
    void whenUpdateCartItem_thenReturnUpdatedCartItem() throws Exception {
        Long itemId = 1L;
        Integer newQuantity = 10;
        CartItem updatedCartItem = new CartItem();
        updatedCartItem.setProductId(itemId);
        updatedCartItem.setQuantity(newQuantity);

        when(cartService.updateCartItem(eq(testUser), eq(itemId), eq(newQuantity))).thenReturn(updatedCartItem);

        mockMvc.perform(put("/cart/item/{itemId}", itemId).with(user(testUser))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"quantity\":10}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantity").value(newQuantity));

        verify(cartService, times(1)).updateCartItem(eq(testUser), eq(itemId), eq(newQuantity));
    }

    @Test
    void whenRemoveCartItem_thenReturnStatusOk() throws Exception {
        Long itemId = 1L;
        doNothing().when(cartService).removeCartItem(testUser, itemId);

        mockMvc.perform(delete("/cart/item/{itemId}", itemId).with(user(testUser)))
                .andExpect(status().isOk());

        verify(cartService, times(1)).removeCartItem(testUser, itemId);
    }

    @Test
    void getCurrentCart_ReturnsCartForAuthenticatedUser() throws Exception {
        Cart cart = new Cart();
        when(cartService.getCurrentCart(any(LocalUser.class))).thenReturn(cart);

        mockMvc.perform(get("/cart").with(user(testUser)))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"));

        verify(cartService, times(1)).getCurrentCart(any(LocalUser.class));
    }
}

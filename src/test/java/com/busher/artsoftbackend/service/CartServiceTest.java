package com.busher.artsoftbackend.service;

import com.busher.artsoftbackend.api.dto.CartSummary;
import com.busher.artsoftbackend.dao.CartItemRepository;
import com.busher.artsoftbackend.dao.CartRepository;
import com.busher.artsoftbackend.model.Cart;
import com.busher.artsoftbackend.model.CartItem;
import com.busher.artsoftbackend.model.LocalUser;
import com.busher.artsoftbackend.model.Product;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CartServiceTest {

    private final Long userId = 1L;
    @Mock
    private ProductService productService;
    @Mock
    private CartRepository cartRepository;
    @Mock
    private CartItemRepository cartItemRepository;
    @InjectMocks
    private CartService cartService;
    private AutoCloseable autoCloseable;
    private LocalUser user;
    private Product product1, product2;

    @BeforeEach
    public void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);

        user = new LocalUser();
        user.setId(1L);

        product1 = new Product(1L, "ASG Gun", "Simple ASG Gun for beginners.",
                "Lorem ipsum dolor sit amet", 22.50, null);
        product2 = new Product(2L, "ASG Gun2", "Simple ASG Gun for beginners2.",
                "Lorem ipsum dolor sit amet2", 10.50, null);
    }

    @AfterEach
    public void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void whenAddProductToCart_thenCartItemIsAdded() {
        Long productId = 1L;
        Integer quantity = 5;

        Cart cart = new Cart();
        cart.setUser(user);
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        cartService.addProductToCart(user, productId, quantity);

        verify(cartRepository, times(1)).findByUserId(userId);
        verify(cartRepository, times(1)).save(cart);
    }

    @Test
    void whenUpdateCartItem_thenQuantityIsUpdated() {
        Long itemId = 1L;
        Integer newQuantity = 10;
        LocalUser user = new LocalUser();
        user.setId(userId);

        CartItem cartItem = new CartItem();
        cartItem.setId(itemId);
        cartItem.setQuantity(5);

        when(cartItemRepository.findByIdAndCartUserId(itemId, userId)).thenReturn(Optional.of(cartItem));
        when(cartItemRepository.save(any(CartItem.class))).thenReturn(cartItem);

        cartService.updateCartItem(user, itemId, newQuantity);

        verify(cartItemRepository).findByIdAndCartUserId(itemId, userId);
        verify(cartItemRepository).save(cartItem);
        assertEquals(newQuantity, cartItem.getQuantity());
    }

    @Test
    void whenRemoveCartItem_thenCartItemIsDeleted() {
        Long itemId = 1L;

        when(cartItemRepository.existsByIdAndCartUserId(itemId, userId)).thenReturn(true);

        cartService.removeCartItem(user, itemId);

        verify(cartItemRepository).deleteById(itemId);
    }

    @Test
    void whenCartExistsForUser_thenReturnCart() {
        Cart existingCart = new Cart();
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(existingCart));

        Cart cart = cartService.getCurrentCart(user);

        assertEquals(existingCart, cart);
        verify(cartRepository, times(1)).findByUserId(userId);
    }

    @Test
    void whenCartDoesNotExistForUser_thenCreateNewCart() {
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.empty());
        when(cartRepository.save(any(Cart.class))).thenAnswer(i -> i.getArgument(0));

        Cart newCart = cartService.getCurrentCart(user);

        assertNotNull(newCart);
        verify(cartRepository, times(1)).findByUserId(userId);
        verify(cartRepository, times(1)).save(any(Cart.class));
    }

    @Test
    void whenCartHasItems_thenReturnsCorrectSummary() {
        Cart cart = new Cart();
        cart.setUser(user);
        cart.setItems(new HashSet<>(Arrays.asList(new CartItem(cart, 1L, 2),
                new CartItem(cart, 2L, 3))));
        when(cartRepository.findByUserId(user.getId())).thenReturn(Optional.of(cart));
        when(productService.getProductsByIds(new HashSet<>(Arrays.asList(1L, 2L))))
                .thenReturn(Arrays.asList(product1, product2));

        CartSummary summary = cartService.getCartSummary(user);

        assertEquals(5, summary.itemCount());
        assertEquals(76.5, summary.totalCost());
    }

    @Test
    void whenSomeProductsAreMissing_thenHandlesGracefully() {
        Cart cart = new Cart();
        cart.setUser(user);
        cart.setItems(new HashSet<>(Arrays.asList(new CartItem(cart, 1L, 2),
                new CartItem(cart, 333L, 6))));
        when(cartRepository.findByUserId(user.getId())).thenReturn(Optional.of(cart));

        when(productService.getProductsByIds(new HashSet<>(Arrays.asList(1L, 333L))))
                .thenReturn(Collections.singletonList(product1));

        CartSummary summary = cartService.getCartSummary(user);

        assertEquals(2, summary.itemCount());
        assertEquals(45, summary.totalCost());
    }

}
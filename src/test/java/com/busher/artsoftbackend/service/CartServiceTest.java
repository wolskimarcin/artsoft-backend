package com.busher.artsoftbackend.service;

import com.busher.artsoftbackend.dao.CartItemRepository;
import com.busher.artsoftbackend.dao.CartRepository;
import com.busher.artsoftbackend.model.Cart;
import com.busher.artsoftbackend.model.CartItem;
import com.busher.artsoftbackend.model.LocalUser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CartServiceTest {

    @Mock
    private CartRepository cartRepository;
    @Mock
    private CartItemRepository cartItemRepository;
    @InjectMocks
    private CartService cartService;
    private AutoCloseable autoCloseable;

    @BeforeEach
    public void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    public void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void whenAddProductToCart_thenCartItemIsAdded() {
        Long userId = 1L;
        Long productId = 1L;
        Integer quantity = 5;
        LocalUser user = new LocalUser();
        user.setId(userId);

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
        Long userId = 1L;
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
        Long userId = 1L;
        Long itemId = 1L;
        LocalUser user = new LocalUser();
        user.setId(userId);

        when(cartItemRepository.existsByIdAndCartUserId(itemId, userId)).thenReturn(true);

        cartService.removeCartItem(user, itemId);

        verify(cartItemRepository).deleteById(itemId);
    }
}
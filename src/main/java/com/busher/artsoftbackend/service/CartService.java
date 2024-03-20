package com.busher.artsoftbackend.service;

import com.busher.artsoftbackend.api.dto.CartSummary;
import com.busher.artsoftbackend.dao.CartItemRepository;
import com.busher.artsoftbackend.dao.CartRepository;
import com.busher.artsoftbackend.model.Cart;
import com.busher.artsoftbackend.model.CartItem;
import com.busher.artsoftbackend.model.LocalUser;
import com.busher.artsoftbackend.model.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductService productService;

    public CartService(CartRepository cartRepository, CartItemRepository cartItemRepository, ProductService productService) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.productService = productService;
    }

    @Transactional
    public CartItem addProductToCart(LocalUser user, Long productId, Integer quantity) {
        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    return cartRepository.save(newCart);
                });

        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getProductId().equals(productId))
                .findFirst();

        CartItem cartItem = existingItem.orElseGet(() -> new CartItem(cart, productId, 0));
        cartItem.setQuantity(cartItem.getQuantity() + quantity);
        cart.getItems().add(cartItem);

        cartRepository.save(cart);
        return cartItem;
    }

    @Transactional
    public void removeCartItem(LocalUser user, Long itemId) {
        if (!cartItemRepository.existsByIdAndCartUserId(itemId, user.getId())) {
            throw new IllegalArgumentException("Invalid cartItem ID or cart does not belong to user: " + itemId);
        }
        cartItemRepository.deleteById(itemId);
    }

    @Transactional
    public CartItem updateCartItem(LocalUser user, Long itemId, Integer quantity) {
        CartItem cartItem = cartItemRepository.findByIdAndCartUserId(itemId, user.getId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid cartItem ID or cart does not belong to user"));
        cartItem.setQuantity(quantity);
        return cartItemRepository.save(cartItem);
    }

    @Transactional
    public Cart getCurrentCart(LocalUser user) {
        return cartRepository.findByUserId(user.getId()).orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setUser(user);
            return cartRepository.save(newCart);
        });
    }

    public CartSummary getCartSummary(LocalUser user) {
        Cart cart = getCurrentCart(user);

        Set<Long> productIds = cart.getItems().stream()
                .map(CartItem::getProductId)
                .collect(Collectors.toSet());
        List<Product> products = productService.getProductsByIds(productIds);

        Map<Long, Product> productMap = products.stream()
                .collect(Collectors.toMap(Product::getId, Function.identity()));

        double totalCost = 0;
        int itemCount = 0;
        for (CartItem item : cart.getItems()) {
            Product product = productMap.get(item.getProductId());
            if (product != null) {
                totalCost += product.getPrice() * item.getQuantity();
                itemCount += item.getQuantity();
            }
        }

        return new CartSummary(itemCount, totalCost);
    }


}

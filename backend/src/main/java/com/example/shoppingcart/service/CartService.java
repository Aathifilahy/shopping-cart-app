package com.example.shoppingcart.service;

import com.example.shoppingcart.model.Cart;
import com.example.shoppingcart.model.Cart.CartItem;
import com.example.shoppingcart.model.Product;
import com.example.shoppingcart.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {

	private final CartRepository cartRepository;
	private final ProductService productService;

	public Cart getCartByUserOrSession(String userId, String sessionId) {
		Optional<Cart> cart = Optional.empty();
		if (userId != null) {
			cart = cartRepository.findByUserId(userId);
		}
		if (cart.isEmpty() && sessionId != null) {
			cart = cartRepository.findBySessionId(sessionId);
		}
		return cart.orElseGet(() -> {
			Cart newCart = new Cart();
			if (userId != null) newCart.setUserId(userId);
			if (sessionId != null) newCart.setSessionId(sessionId);
			return cartRepository.save(newCart);
		});
	}

	@Transactional
	public Cart addItem(String userId, String sessionId, String productId, int quantity) {
		Cart cart = getCartByUserOrSession(userId, sessionId);
		Product product = productService.getProduct(productId);

		Optional<CartItem> existing = cart.getItems().stream()
				.filter(item -> item.getProductId().equals(productId))
				.findFirst();

		if (existing.isPresent()) {
			existing.get().setQuantity(existing.get().getQuantity() + quantity);
		} else {
			CartItem newItem = new CartItem();
			newItem.setProductId(productId);
			newItem.setQuantity(quantity);
			newItem.setPriceSnapshot(product.getPrice());
			cart.getItems().add(newItem);
		}
		recalculateTotal(cart);
		return cartRepository.save(cart);
	}

	@Transactional
	public Cart updateItemQuantity(String userId, String sessionId, String productId, int quantity) {
		Cart cart = getCartByUserOrSession(userId, sessionId);
		cart.getItems().stream()
				.filter(item -> item.getProductId().equals(productId))
				.findFirst()
				.ifPresent(item -> item.setQuantity(quantity));
		recalculateTotal(cart);
		return cartRepository.save(cart);
	}

	@Transactional
	public Cart removeItem(String userId, String sessionId, String productId) {
		Cart cart = getCartByUserOrSession(userId, sessionId);
		cart.getItems().removeIf(item -> item.getProductId().equals(productId));
		recalculateTotal(cart);
		return cartRepository.save(cart);
	}

	@Transactional
	public Cart clearCart(String userId, String sessionId) {
		Cart cart = getCartByUserOrSession(userId, sessionId);
		cart.getItems().clear();
		cart.setTotal(0);
		return cartRepository.save(cart);
	}

	public Cart mergeGuestCart(String userId, String sessionId) {
		Optional<Cart> guestCartOpt = cartRepository.findBySessionId(sessionId);
		if (guestCartOpt.isEmpty()) {
			return getCartByUserOrSession(userId, null);
		}

		Cart guestCart = guestCartOpt.get();
		Cart userCart = getCartByUserOrSession(userId, null);

		for (CartItem guestItem : guestCart.getItems()) {
			Optional<CartItem> existing = userCart.getItems().stream()
					.filter(item -> item.getProductId().equals(guestItem.getProductId()))
					.findFirst();
			if (existing.isPresent()) {
				existing.get().setQuantity(existing.get().getQuantity() + guestItem.getQuantity());
			} else {
				userCart.getItems().add(guestItem);
			}
		}
		recalculateTotal(userCart);
		cartRepository.delete(guestCart);
		return cartRepository.save(userCart);
	}

	private void recalculateTotal(Cart cart) {
		double total = cart.getItems().stream()
				.mapToDouble(item -> item.getPriceSnapshot() * item.getQuantity())
				.sum();
		cart.setTotal(total);
	}
}

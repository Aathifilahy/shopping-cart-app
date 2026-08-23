package com.example.shoppingcart.controller;

import com.example.shoppingcart.model.Cart;
import com.example.shoppingcart.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

	private final CartService cartService;

	private String getSessionId(HttpServletRequest request) {
		return request.getHeader("X-Session-Id");
	}

	private String getUserId(Authentication authentication) {
		if (authentication != null && authentication.isAuthenticated()) {
			return authentication.getName();
		}
		return null;
	}

	@GetMapping
	public Cart getCart(HttpServletRequest request, Authentication auth) {
		String userId = getUserId(auth);
		String sessionId = getSessionId(request);
		return cartService.getCartByUserOrSession(userId, sessionId);
	}

	@PostMapping("/items")
	public Cart addItem(@RequestBody Map<String, Object> payload,
						HttpServletRequest request, Authentication auth) {
		String productId = (String) payload.get("productId");
		int quantity = (int) payload.getOrDefault("quantity", 1);
		String userId = getUserId(auth);
		String sessionId = getSessionId(request);
		return cartService.addItem(userId, sessionId, productId, quantity);
	}

	@PutMapping("/items/{productId}")
	public Cart updateItem(@PathVariable String productId,
						   @RequestBody Map<String, Integer> payload,
						   HttpServletRequest request, Authentication auth) {
		int quantity = payload.getOrDefault("quantity", 0);
		String userId = getUserId(auth);
		String sessionId = getSessionId(request);
		return cartService.updateItemQuantity(userId, sessionId, productId, quantity);
	}

	@DeleteMapping("/items/{productId}")
	public Cart removeItem(@PathVariable String productId,
						   HttpServletRequest request, Authentication auth) {
		String userId = getUserId(auth);
		String sessionId = getSessionId(request);
		return cartService.removeItem(userId, sessionId, productId);
	}

	@DeleteMapping
	public Cart clearCart(HttpServletRequest request, Authentication auth) {
		String userId = getUserId(auth);
		String sessionId = getSessionId(request);
		return cartService.clearCart(userId, sessionId);
	}

	@PostMapping("/merge")
	public Cart mergeGuestCart(@RequestBody Map<String, String> payload,
							   Authentication auth) {
		String userId = getUserId(auth);
		String sessionId = payload.get("sessionId");
		return cartService.mergeGuestCart(userId, sessionId);
	}
}

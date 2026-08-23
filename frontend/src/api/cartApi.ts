import client from './client';
import { Cart } from '../types/Cart';

export const getCart = () =>
	client.get<Cart>('/cart');

export const addToCart = (productId: string, quantity: number = 1) =>
	client.post<Cart>('/cart/items', { productId, quantity });

export const updateCartItem = (productId: string, quantity: number) =>
	client.put<Cart>(`/cart/items/${productId}`, { quantity });

export const removeFromCart = (productId: string) =>
	client.delete<Cart>(`/cart/items/${productId}`);

export const clearCart = () =>
	client.delete<Cart>('/cart');

export const mergeGuestCart = (sessionId: string) =>
	client.post<Cart>('/cart/merge', { sessionId });

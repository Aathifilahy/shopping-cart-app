import React, { createContext, useState, useEffect, ReactNode } from 'react';
import { Cart } from '../types/Cart';
import { getCart, addToCart, updateCartItem, removeFromCart, clearCart, mergeGuestCart } from '../api/cartApi';
import { useAuth } from './AuthContext';

interface CartContextType {
  cart: Cart | null;
  loading: boolean;
  addItem: (productId: string, quantity?: number) => Promise<void>;
  updateItem: (productId: string, quantity: number) => Promise<void>;
  removeItem: (productId: string) => Promise<void>;
  clear: () => Promise<void>;
  mergeGuest: () => Promise<void>;
  refreshCart: () => Promise<void>;
}

const CartContext = createContext<CartContextType | undefined>(undefined);

export const CartProvider: React.FC<{ children: ReactNode }> = ({ children }) => {
  const [cart, setCart] = useState<Cart | null>(null);
  const [loading, setLoading] = useState(false);
  const { isAuthenticated } = useAuth();

  // Generate sessionId for guest if not exists
  useEffect(() => {
    if (!localStorage.getItem('sessionId')) {
      localStorage.setItem('sessionId', crypto.randomUUID());
    }
  }, []);

  const refreshCart = async () => {
    setLoading(true);
    try {
      const res = await getCart();
      setCart(res.data);
    } catch (error) {
      console.error('Failed to fetch cart:', error);
    } finally {
      setLoading(false);
    }
  };

  // Auto-fetch cart only when authenticated (or we can also fetch for guest)
  useEffect(() => {
    if (isAuthenticated) {
      refreshCart();
    } else {
      // For guest, we can still fetch the cart using sessionId
      // but to avoid 401, we need to ensure the backend allows guest cart.
      // The backend does allow guest carts via sessionId.
      // So we can fetch it even if not authenticated.
      // But let's fetch it anyway – the backend will handle it.
      refreshCart();
    }
  }, [isAuthenticated]);

  const addItem = async (productId: string, quantity = 1) => {
    const res = await addToCart(productId, quantity);
    setCart(res.data);
  };

  const updateItem = async (productId: string, quantity: number) => {
    const res = await updateCartItem(productId, quantity);
    setCart(res.data);
  };

  const removeItem = async (productId: string) => {
    const res = await removeFromCart(productId);
    setCart(res.data);
  };

  const clear = async () => {
    const res = await clearCart();
    setCart(res.data);
  };

  const mergeGuest = async () => {
    const sessionId = localStorage.getItem('sessionId')!;
    const res = await mergeGuestCart(sessionId);
    setCart(res.data);
    localStorage.removeItem('sessionId');
  };

  return (
    <CartContext.Provider
      value={{ cart, loading, addItem, updateItem, removeItem, clear, mergeGuest, refreshCart }}
    >
      {children}
    </CartContext.Provider>
  );
};

export const useCart = () => {
  const context = React.useContext(CartContext);
  if (!context) throw new Error('useCart must be used within CartProvider');
  return context;
};
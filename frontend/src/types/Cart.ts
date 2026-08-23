export interface CartItem {
  productId: string;
  quantity: number;
  priceSnapshot: number;
}

export interface Cart {
  id: string;
  userId: string | null;
  sessionId: string | null;
  items: CartItem[];
  total: number;
  updatedAt: string;
}
export interface Product {
  id: string;
  categoryId: string;
  name: string;
  price: number;
  description: string;
  imageUrl: string;
  inStock: boolean;
  unit: string;
  stockQuantity: number;   // new
  expirationDate?: string; // optional (date string)
}
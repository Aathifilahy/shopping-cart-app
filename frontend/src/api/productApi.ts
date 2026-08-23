import client from './client';
import { Product } from '../types/Product';

export const getProducts = () =>
	client.get<Product[]>('/products');

export const getProduct = (id: string) =>
	client.get<Product>(`/products/${id}`);

export const getProductsByCategory = (categoryId: string) =>
	client.get<Product[]>(`/products/category/${categoryId}`);

export const searchProducts = (query: string) =>
	client.get<Product[]>(`/products/search?q=${encodeURIComponent(query)}`);

export const createProduct = (data: Partial<Product>) =>
	client.post<Product>('/admin/products', data);

export const updateProduct = (id: string, data: Partial<Product>) =>
	client.put<Product>(`/admin/products/${id}`, data);

export const deleteProduct = (id: string) =>
	client.delete<void>(`/admin/products/${id}`);

import client from './client';
import { Category } from '../types/Category';

export const getCategories = () =>
	client.get<Category[]>('/categories');

export const getCategory = (id: string) =>
	client.get<Category>(`/categories/${id}`);

export const createCategory = (data: Partial<Category>) =>
	client.post<Category>('/admin/categories', data);

export const updateCategory = (id: string, data: Partial<Category>) =>
	client.put<Category>(`/admin/categories/${id}`, data);

export const deleteCategory = (id: string) =>
	client.delete<void>(`/admin/categories/${id}`);

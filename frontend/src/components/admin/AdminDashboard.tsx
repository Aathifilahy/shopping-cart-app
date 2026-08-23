import React, { useState, useEffect } from 'react';
import { Product } from '../../types/Product';
import { Category } from '../../types/Category';
import { getProducts, createProduct, updateProduct, deleteProduct } from '../../api/productApi';
import { getCategories, createCategory, updateCategory, deleteCategory } from '../../api/categoryApi';
import ProductForm from './ProductForm';
import CategoryForm from './CategoryForm';
import toast from 'react-hot-toast';

const AdminDashboard: React.FC = () => {
  const [products, setProducts] = useState<Product[]>([]);
  const [categories, setCategories] = useState<Category[]>([]);
  const [selectedProduct, setSelectedProduct] = useState<Product | null>(null);
  const [selectedCategory, setSelectedCategory] = useState<Category | null>(null);
  const [showProductForm, setShowProductForm] = useState(false);
  const [showCategoryForm, setShowCategoryForm] = useState(false);

  useEffect(() => {
    fetchData();
  }, []);

  const fetchData = async () => {
    try {
      const [prodRes, catRes] = await Promise.all([getProducts(), getCategories()]);
      setProducts(prodRes.data);
      setCategories(catRes.data);
    } catch (error) {
      console.error(error);
      toast.error('Failed to fetch data');
    }
  };

  const handleProductSubmit = async (data: Partial<Product>) => {
    try {
      if (selectedProduct) {
        await updateProduct(selectedProduct.id, data);
        toast.success('Product updated');
      } else {
        await createProduct(data);
        toast.success('Product created');
      }
      setShowProductForm(false);
      setSelectedProduct(null);
      fetchData();
    } catch (error) {
      console.error(error);
      toast.error('Operation failed');
    }
  };

  const handleCategorySubmit = async (data: Partial<Category>) => {
    try {
      if (selectedCategory) {
        await updateCategory(selectedCategory.id, data);
        toast.success('Category updated');
      } else {
        await createCategory(data);
        toast.success('Category created');
      }
      setShowCategoryForm(false);
      setSelectedCategory(null);
      fetchData();
    } catch (error) {
      console.error(error);
      toast.error('Operation failed');
    }
  };

  const handleDeleteProduct = async (id: string) => {
    if (window.confirm('Delete this product?')) {
      try {
        await deleteProduct(id);
        toast.success('Product deleted');
        fetchData();
      } catch (error) {
        console.error(error);
        toast.error('Delete failed');
      }
    }
  };

  const handleDeleteCategory = async (id: string) => {
    if (window.confirm('Delete this category?')) {
      try {
        await deleteCategory(id);
        toast.success('Category deleted');
        fetchData();
      } catch (error) {
        console.error(error);
        toast.error('Delete failed');
      }
    }
  };

  return (
    <div className="space-y-8">
      <div>
        <div className="flex justify-between items-center mb-4">
          <h2 className="text-2xl font-bold">Categories</h2>
          <button
            onClick={() => { setSelectedCategory(null); setShowCategoryForm(true); }}
            className="bg-green-600 text-white px-4 py-2 rounded hover:bg-green-700"
          >
            Add Category
          </button>
        </div>
        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
          {categories.map(cat => (
            <div key={cat.id} className="bg-white shadow rounded p-4 flex justify-between items-center">
              <span>{cat.name}</span>
              <div className="space-x-2">
                <button
                  onClick={() => { setSelectedCategory(cat); setShowCategoryForm(true); }}
                  className="text-blue-600 hover:underline"
                >
                  Edit
                </button>
                <button
                  onClick={() => handleDeleteCategory(cat.id)}
                  className="text-red-600 hover:underline"
                >
                  Delete
                </button>
              </div>
            </div>
          ))}
        </div>
      </div>

      <div>
        <div className="flex justify-between items-center mb-4">
          <h2 className="text-2xl font-bold">Products</h2>
          <button
            onClick={() => { setSelectedProduct(null); setShowProductForm(true); }}
            className="bg-green-600 text-white px-4 py-2 rounded hover:bg-green-700"
          >
            Add Product
          </button>
        </div>
        <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
          {products.map(prod => (
            <div key={prod.id} className="bg-white shadow rounded p-4">
              <h3 className="font-bold">{prod.name}</h3>
              <p className="text-gray-600 text-sm">${prod.price}</p>
              <div className="mt-2 space-x-2">
                <button
                  onClick={() => { setSelectedProduct(prod); setShowProductForm(true); }}
                  className="text-blue-600 hover:underline"
                >
                  Edit
                </button>
                <button
                  onClick={() => handleDeleteProduct(prod.id)}
                  className="text-red-600 hover:underline"
                >
                  Delete
                </button>
              </div>
            </div>
          ))}
        </div>
      </div>

      {showProductForm && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center p-4">
          <div className="bg-white rounded-lg p-6 w-full max-w-md max-h-screen overflow-y-auto">
            <h2 className="text-xl font-bold mb-4">
              {selectedProduct ? 'Edit Product' : 'Add Product'}
            </h2>
            <ProductForm
              initialData={selectedProduct || undefined}
              onSubmit={handleProductSubmit}
              onCancel={() => { setShowProductForm(false); setSelectedProduct(null); }}
            />
          </div>
        </div>
      )}

      {showCategoryForm && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center p-4">
          <div className="bg-white rounded-lg p-6 w-full max-w-md">
            <h2 className="text-xl font-bold mb-4">
              {selectedCategory ? 'Edit Category' : 'Add Category'}
            </h2>
            <CategoryForm
              initialData={selectedCategory || undefined}
              onSubmit={handleCategorySubmit}
              onCancel={() => { setShowCategoryForm(false); setSelectedCategory(null); }}
            />
          </div>
        </div>
      )}
    </div>
  );
};

export default AdminDashboard;
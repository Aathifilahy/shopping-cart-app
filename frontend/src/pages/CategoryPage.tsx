import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { getCategory } from '../api/categoryApi';
import { getProductsByCategory } from '../api/productApi';
import { Category } from '../types/Category';
import { Product } from '../types/Product';
import ProductGrid from '../components/product/ProductGrid';
import Loader from '../components/common/Loader';

const CategoryPage: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const [category, setCategory] = useState<Category | null>(null);
  const [products, setProducts] = useState<Product[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    if (id) {
      setLoading(true);
      Promise.all([getCategory(id), getProductsByCategory(id)])
        .then(([catRes, prodRes]) => {
          setCategory(catRes.data);
          setProducts(prodRes.data);
        })
        .finally(() => setLoading(false));
    }
  }, [id]);

  if (loading) return <Loader />;
  if (!category) return <div>Category not found</div>;

  return (
    <div className="container mx-auto px-4 py-8">
      <h1 className="text-3xl font-bold mb-2">{category.name}</h1>
      <p className="text-gray-600 mb-6">{category.description}</p>
      <ProductGrid products={products} />
    </div>
  );
};

export default CategoryPage;
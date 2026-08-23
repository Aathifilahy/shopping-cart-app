import React, { useEffect } from 'react';
import { useProducts } from '../hooks/useProducts';
import ProductGrid from '../components/product/ProductGrid';
import CategoryList from '../components/category/CategoryList';
import Loader from '../components/common/Loader';

const HomePage: React.FC = () => {
  const { products, loading, fetchProducts } = useProducts();

  useEffect(() => {
    fetchProducts();
  }, []);

  return (
    <div className="container mx-auto px-4 py-8">
      <section className="mb-8">
        <h2 className="text-2xl font-bold mb-4">Categories</h2>
        <CategoryList />
      </section>
      <section>
        <h2 className="text-2xl font-bold mb-4">Featured Products</h2>
        {loading ? <Loader /> : <ProductGrid products={products.slice(0, 8)} />}
      </section>
    </div>
  );
};

export default HomePage;
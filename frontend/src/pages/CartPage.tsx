import React, { useEffect, useState } from 'react';
import { useCart } from '../context/CartContext';
import { useProducts } from '../hooks/useProducts';
import CartItem from '../components/cart/CartItem';
import CartSummary from '../components/cart/CartSummary';
import CartEmpty from '../components/cart/CartEmpty';
import Loader from '../components/common/Loader';
import { Product } from '../types/Product';

const CartPage: React.FC = () => {
  const { cart, loading: cartLoading, refreshCart } = useCart();
  const { products, loading: productsLoading, fetchProducts } = useProducts();
  const [productMap, setProductMap] = useState<Map<string, Product>>(new Map());

  useEffect(() => {
    fetchProducts();
    refreshCart();
  }, []);

  useEffect(() => {
    if (products.length > 0) {
      const map = new Map<string, Product>();
      products.forEach(p => map.set(p.id, p));
      setProductMap(map);
    }
  }, [products]);

  if (cartLoading || productsLoading) return <Loader />;
  if (!cart || cart.items.length === 0) return <CartEmpty />;

  return (
    <div className="container mx-auto px-4 py-8">
      <h1 className="text-3xl font-bold mb-6">Your Cart</h1>
      <div className="flex flex-col lg:flex-row gap-8">
        <div className="flex-1">
          {cart.items.map((item) => {
            const product = productMap.get(item.productId);
            return product ? (
              <CartItem
                key={item.productId}
                item={item}
                productName={product.name}
                unit={product.unit}
              />
            ) : null;
          })}
        </div>
        <div className="lg:w-80">
          <CartSummary cart={cart} />
        </div>
      </div>
    </div>
  );
};

export default CartPage;
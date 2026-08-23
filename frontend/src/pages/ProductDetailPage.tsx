import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { getProduct } from '../api/productApi';
import { Product } from '../types/Product';
import { formatCurrency } from '../utils/currencyFormatter';
import { useCart } from '../context/CartContext';
import Loader from '../components/common/Loader';
import toast from 'react-hot-toast';

const ProductDetailPage: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const [product, setProduct] = useState<Product | null>(null);
  const [loading, setLoading] = useState(true);
  const [quantity, setQuantity] = useState(1);
  const { addItem } = useCart();

  useEffect(() => {
    if (id) {
      getProduct(id)
        .then((res) => setProduct(res.data))
        .finally(() => setLoading(false));
    }
  }, [id]);

  const handleAddToCart = async () => {
    if (product) {
      await addItem(product.id, quantity);
      toast.success('Added to cart!');
    }
  };

  if (loading) return <Loader />;
  if (!product) return <div>Product not found</div>;

  return (
    <div className="container mx-auto px-4 py-8">
      <div className="flex flex-col md:flex-row gap-8">
        <div className="md:w-1/2">
          <img
            src={product.imageUrl || 'https://via.placeholder.com/600x400'}
            alt={product.name}
            className="w-full rounded-lg shadow"
          />
        </div>
        <div className="md:w-1/2">
          <h1 className="text-3xl font-bold">{product.name}</h1>
          <p className="text-gray-600 mt-2">{product.description}</p>
          <div className="mt-4">
            <span className="text-2xl font-bold text-green-700">
              {formatCurrency(product.price)}
            </span>
            <span className="text-sm text-gray-500 ml-2">per {product.unit}</span>
          </div>
          <div className="mt-6 flex items-center space-x-4">
            <label htmlFor="quantity" className="font-medium">Qty:</label>
            <input
              id="quantity"
              type="number"
              min="1"
              value={quantity}
              onChange={(e) => setQuantity(Math.max(1, parseInt(e.target.value) || 1))}
              className="w-20 border rounded px-3 py-2"
            />
            <button
              onClick={handleAddToCart}
              className="bg-green-600 text-white px-6 py-2 rounded-lg hover:bg-green-700 transition"
              disabled={!product.inStock}
            >
              {product.inStock ? 'Add to Cart' : 'Out of Stock'}
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default ProductDetailPage;
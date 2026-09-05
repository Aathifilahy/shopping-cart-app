import React from 'react';
import { Product } from '../../types/Product';
import { formatCurrency } from '../../utils/currencyFormatter';
import { Link } from 'react-router-dom';
import { useCart } from '../../context/CartContext';
import { useAuth } from '../../context/AuthContext';

interface ProductCardProps {
  product: Product;
}

const ProductCard: React.FC<ProductCardProps> = ({ product }) => {
  const { addItem } = useCart();
  const { user } = useAuth();
  const isAdmin = user?.roles?.includes('ADMIN');

  const handleAddToCart = (e: React.MouseEvent) => {
    e.preventDefault();
    addItem(product.id);
  };

  return (
    <div className="bg-white rounded-lg shadow-md overflow-hidden hover:shadow-lg transition">
      <Link to={`/product/${product.id}`}>
        <img
          src={product.imageUrl || 'https://via.placeholder.com/300x200'}
          alt={product.name}
          className="w-full h-48 object-cover"
        />
        <div className="p-4">
          <h3 className="font-semibold text-lg">{product.name}</h3>
          <p className="text-gray-600 text-sm truncate">{product.description}</p>
          <div className="flex justify-between items-center mt-2">
            <span className="text-green-700 font-bold">{formatCurrency(product.price)}</span>
            {!isAdmin && (
              <button
                onClick={handleAddToCart}
                className="bg-green-600 text-white px-3 py-1 rounded hover:bg-green-700 text-sm"
              >
                Add to Cart
              </button>
            )}
          </div>
        </div>
      </Link>
    </div>
  );
};

export default ProductCard;
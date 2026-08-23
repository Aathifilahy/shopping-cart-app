import React from 'react';
import { Link } from 'react-router-dom';

const CartEmpty: React.FC = () => {
  return (
    <div className="text-center py-10">
      <h2 className="text-2xl font-semibold text-gray-600">Your cart is empty</h2>
      <p className="text-gray-500 mt-2">Start adding items to your cart.</p>
      <Link to="/" className="mt-4 inline-block bg-green-600 text-white px-6 py-2 rounded hover:bg-green-700">
        Continue Shopping
      </Link>
    </div>
  );
};

export default CartEmpty;
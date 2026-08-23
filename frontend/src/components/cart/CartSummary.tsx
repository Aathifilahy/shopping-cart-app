import React from 'react';
import { Cart } from '../../types/Cart';
import { formatCurrency } from '../../utils/currencyFormatter';

interface CartSummaryProps {
  cart: Cart;
}

const CartSummary: React.FC<CartSummaryProps> = ({ cart }) => {
  return (
    <div className="bg-gray-50 p-4 rounded-lg">
      <div className="flex justify-between text-lg font-bold">
        <span>Total:</span>
        <span className="text-green-700">{formatCurrency(cart.total)}</span>
      </div>
      <button className="w-full mt-4 bg-green-600 text-white py-2 rounded-lg hover:bg-green-700 transition">
        Proceed to Checkout
      </button>
    </div>
  );
};

export default CartSummary;
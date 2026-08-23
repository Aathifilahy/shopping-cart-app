import React from 'react';
import { CartItem as CartItemType } from '../../types/Cart';
import { formatCurrency } from '../../utils/currencyFormatter';
import { useCart } from '../../context/CartContext';

interface CartItemProps {
  item: CartItemType;
  productName: string;
  unit: string;
}

const CartItem: React.FC<CartItemProps> = ({ item, productName, unit }) => {
  const { updateItem, removeItem } = useCart();

  const handleQuantityChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const qty = parseInt(e.target.value) || 0;
    if (qty > 0) {
      updateItem(item.productId, qty);
    } else {
      removeItem(item.productId);
    }
  };

  return (
    <div className="flex items-center justify-between border-b py-3">
      <div className="flex-1">
        <h4 className="font-medium">{productName}</h4>
        <p className="text-sm text-gray-500">
          {formatCurrency(item.priceSnapshot)} / {unit}
        </p>
      </div>
      <div className="flex items-center space-x-4">
        <input
          type="number"
          min="1"
          value={item.quantity}
          onChange={handleQuantityChange}
          className="w-16 border rounded px-2 py-1 text-center"
        />
        <span className="font-semibold w-20 text-right">
          {formatCurrency(item.priceSnapshot * item.quantity)}
        </span>
        <button
          onClick={() => removeItem(item.productId)}
          className="text-red-500 hover:text-red-700"
        >
          ✕
        </button>
      </div>
    </div>
  );
};

export default CartItem;
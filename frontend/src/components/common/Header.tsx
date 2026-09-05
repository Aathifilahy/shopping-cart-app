import React from 'react';
import { Link } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import { useCart } from '../../context/CartContext';
import CartIcon from './CartIcon';

const Header: React.FC = () => {
  const { user, logout } = useAuth();
  const { cart } = useCart();
  const itemCount = cart?.items?.reduce((acc, item) => acc + item.quantity, 0) || 0;

  const isAdmin = user?.roles?.includes('ADMIN');

  return (
    <header className="bg-white shadow-md">
      <div className="container mx-auto px-4 py-3 flex justify-between items-center">
        <Link to="/" className="text-2xl font-bold text-green-600">
          ShopFresh
        </Link>
        <nav className="flex items-center space-x-6">
          <Link to="/categories" className="hover:text-green-600">Categories</Link>

          {/* Admin Panel button – only for admins */}
          {isAdmin && (
            <Link to="/admin" className="hover:text-green-600 font-medium text-blue-600">
              Admin Panel
            </Link>
          )}

          {/* Cart – hidden for admins */}
          {!isAdmin && (
            <Link to="/cart" className="relative">
              <CartIcon />
              {itemCount > 0 && (
                <span className="absolute -top-2 -right-2 bg-red-500 text-white text-xs rounded-full h-5 w-5 flex items-center justify-center">
                  {itemCount}
                </span>
              )}
            </Link>
          )}

          {user ? (
            <div className="flex items-center space-x-4">
              <span className="text-sm">{user.name}</span>
              <button
                onClick={logout}
                className="bg-red-500 text-white px-3 py-1 rounded hover:bg-red-600"
              >
                Logout
              </button>
            </div>
          ) : (
            <Link to="/login" className="bg-green-600 text-white px-4 py-2 rounded hover:bg-green-700">
              Login
            </Link>
          )}
        </nav>
      </div>
    </header>
  );
};

export default Header;
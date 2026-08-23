import React from 'react';
import { Routes, Route, Navigate } from 'react-router-dom';
import HomePage from '../pages/HomePage';
import CategoryPage from '../pages/CategoryPage';
import ProductDetailPage from '../pages/ProductDetailPage';
import CartPage from '../pages/CartPage';
import LoginPage from '../pages/LoginPage';
import AdminPage from '../pages/AdminPage';
import CheckoutPage from '../pages/CheckoutPage';
import OAuth2RedirectPage from '../pages/OAuth2RedirectPage'; // <-- ADD THIS
import { useAuth } from '../context/AuthContext';
import Loader from '../components/common/Loader';

const AppRoutes: React.FC = () => {
  const { user, loading } = useAuth();
  const isAdmin = user?.roles?.includes('ADMIN');

  // Wait until authentication check is complete
  if (loading) {
    return <Loader />;
  }

  return (
    <Routes>
      {/* Public routes – no login required */}
      <Route path="/login" element={<LoginPage />} />
      <Route path="/oauth2/redirect" element={<OAuth2RedirectPage />} /> {/* <-- ADD THIS */}

      {/* Protected routes – redirect to login if not authenticated */}
      <Route path="/" element={user ? <HomePage /> : <Navigate to="/login" />} />
      <Route path="/categories/:id" element={user ? <CategoryPage /> : <Navigate to="/login" />} />
      <Route path="/product/:id" element={user ? <ProductDetailPage /> : <Navigate to="/login" />} />
      <Route path="/cart" element={user ? <CartPage /> : <Navigate to="/login" />} />
      <Route path="/checkout" element={user ? <CheckoutPage /> : <Navigate to="/login" />} />

      {/* Admin route – only for admin users */}
      <Route
        path="/admin"
        element={isAdmin ? <AdminPage /> : <Navigate to="/" />}
      />

      {/* Catch-all */}
      <Route path="*" element={<Navigate to="/" />} />
    </Routes>
  );
};

export default AppRoutes;
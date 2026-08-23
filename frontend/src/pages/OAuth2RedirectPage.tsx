import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import Loader from '../components/common/Loader';

const OAuth2RedirectPage: React.FC = () => {
  const { login, user } = useAuth();
  const navigate = useNavigate();
  const [processed, setProcessed] = useState(false);

  useEffect(() => {
    const params = new URLSearchParams(window.location.search);
    const token = params.get('token');

    if (token) {
      console.log('🔑 OAuth2Redirect: token found, calling login');
      login(token);
      setProcessed(true);
    } else {
      console.log('❌ OAuth2Redirect: no token, redirecting to login');
      navigate('/login');
    }
  }, [login, navigate]);

  // Wait for user to be set before navigating
  useEffect(() => {
    if (processed && user) {
      // Check if user has ADMIN role
      const isAdmin = user.roles?.includes('ADMIN');
      const redirectPath = isAdmin ? '/admin' : '/';
      console.log(`✅ OAuth2Redirect: user logged in, navigating to ${redirectPath}`);
      navigate(redirectPath);
    }
  }, [processed, user, navigate]);

  return <Loader />;
};

export default OAuth2RedirectPage;
import React from 'react';
import { loginWithOAuth2 } from '../../api/authApi';

const GoogleLoginButton: React.FC = () => {
  return (
    <button
      onClick={() => loginWithOAuth2('google')}
      className="w-full flex items-center justify-center bg-red-600 text-white py-2 rounded-lg hover:bg-red-700 transition"
    >
      Sign in with Google
    </button>
  );
};

export default GoogleLoginButton;
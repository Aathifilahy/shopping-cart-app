import React from 'react';
import { loginWithOAuth2 } from '../../api/authApi';

const FacebookLoginButton: React.FC = () => {
  return (
    <button
      onClick={() => loginWithOAuth2('facebook')}
      className="w-full flex items-center justify-center bg-blue-600 text-white py-2 rounded-lg hover:bg-blue-700 transition"
    >
      Sign in with Facebook
    </button>
  );
};

export default FacebookLoginButton;
import React, { useState, useEffect } from 'react';
import { useAuth } from '../context/AuthContext';
import GoogleLoginButton from '../components/auth/GoogleLoginButton';
import FacebookLoginButton from '../components/auth/FacebookLoginButton';
import PasskeyRegister from '../components/auth/PasskeyRegister';

const LoginPage: React.FC = () => {
  const { login } = useAuth();
  const [email, setEmail] = useState('');

  useEffect(() => {
    const params = new URLSearchParams(window.location.search);
    const token = params.get('token');
    if (token) {
      login(token);
      window.history.replaceState({}, document.title, '/');
    }
  }, []);

  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-50">
      <div className="bg-white p-8 rounded-lg shadow-lg w-full max-w-md">
        <h2 className="text-2xl font-bold text-center mb-6">Sign In</h2>
        <div className="space-y-4">
          <GoogleLoginButton />
          <FacebookLoginButton />
          <div className="relative my-4">
            <div className="absolute inset-0 flex items-center">
              <div className="w-full border-t border-gray-300"></div>
            </div>
            <div className="relative flex justify-center text-sm">
              <span className="bg-white px-2 text-gray-500">Or use Passkey</span>
            </div>
          </div>
          <div className="flex items-center space-x-2">
            <input
              type="email"
              placeholder="Email for passkey"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              className="flex-1 border rounded px-3 py-2"
            />
            <PasskeyRegister email={email} />
          </div>
        </div>
      </div>
    </div>
  );
};

export default LoginPage;
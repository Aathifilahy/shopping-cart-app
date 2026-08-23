import React, { useState } from 'react';
import { startPasskeyRegistration, completePasskeyRegistration, startPasskeyLogin, completePasskeyLogin } from '../../api/authApi';
import { createPasskeyCredential, getPasskeyAssertion } from '../../utils/webauthn';
import { useAuth } from '../../context/AuthContext';
import toast from 'react-hot-toast';

interface PasskeyRegisterProps {
  email: string;
}

const PasskeyRegister: React.FC<PasskeyRegisterProps> = ({ email }) => {
  const { login } = useAuth();
  const [loading, setLoading] = useState(false);

  const handleRegister = async () => {
    if (!email) {
      toast.error('Please enter your email');
      return;
    }
    setLoading(true);
    try {
      const { data: options } = await startPasskeyRegistration(email);
      const credential = await createPasskeyCredential(options);
      await completePasskeyRegistration(credential);
      toast.success('Passkey registered successfully!');
    } catch (error) {
      console.error(error);
      toast.error('Registration failed');
    } finally {
      setLoading(false);
    }
  };

  const handleLogin = async () => {
    if (!email) {
      toast.error('Please enter your email');
      return;
    }
    setLoading(true);
    try {
      const { data: options } = await startPasskeyLogin(email);
      const credential = await getPasskeyAssertion(options);
      const { data: authResponse } = await completePasskeyLogin(credential);
      login(authResponse.token);
      toast.success('Logged in with passkey');
    } catch (error) {
      console.error(error);
      toast.error('Login failed');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="flex space-x-2">
      <button
        onClick={handleRegister}
        disabled={loading}
        className="bg-gray-800 text-white px-4 py-2 rounded hover:bg-gray-900 transition"
      >
        Register Passkey
      </button>
      <button
        onClick={handleLogin}
        disabled={loading}
        className="bg-green-600 text-white px-4 py-2 rounded hover:bg-green-700 transition"
      >
        Login with Passkey
      </button>
    </div>
  );
};

export default PasskeyRegister;
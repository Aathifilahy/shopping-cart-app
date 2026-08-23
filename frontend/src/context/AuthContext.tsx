import React, { createContext, useState, useEffect, ReactNode } from 'react';
import { User } from '../types/User';
import client from '../api/client';

interface AuthContextType {
  user: User | null;
  isAuthenticated: boolean;
  loading: boolean;
  login: (token: string) => void;
  logout: () => void;
  setUser: (user: User) => void;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const AuthProvider: React.FC<{ children: ReactNode }> = ({ children }) => {
  const [user, setUser] = useState<User | null>(null);
  const [loading, setLoading] = useState(true);

  const fetchUser = async () => {
    console.log('🔍 fetchUser called');
    try {
      const res = await client.get('/auth/me');
      console.log('✅ User fetched:', res.data);
      setUser(res.data);
    } catch (error) {
      console.error('❌ fetchUser error:', error);
      localStorage.removeItem('jwtToken');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    const token = localStorage.getItem('jwtToken');
    if (token) {
      fetchUser();
    } else {
      setLoading(false);
    }
  }, []);

  const login = (token: string) => {
    console.log('🔑 login called with token:', token);
    localStorage.setItem('jwtToken', token);
    fetchUser();
  };

  const logout = () => {
    localStorage.removeItem('jwtToken');
    setUser(null);
  };

  return (
    <AuthContext.Provider
      value={{ user, isAuthenticated: !!user, loading, login, logout, setUser }}
    >
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => {
  const context = React.useContext(AuthContext);
  if (!context) throw new Error('useAuth must be used within AuthProvider');
  return context;
};
import client from './client';

export const loginWithOAuth2 = (provider: 'google' | 'facebook') => {
	window.location.href = `${client.defaults.baseURL}/oauth2/authorization/${provider}`;
};

export const handleOAuth2Redirect = (token: string) => {
	localStorage.setItem('jwtToken', token);
};

export const logout = () => {
	localStorage.removeItem('jwtToken');
};

export const startPasskeyRegistration = (userId: string) =>
	client.post('/auth/passkey/register/start', { userId });

export const completePasskeyRegistration = (credential: any) =>
	client.post('/auth/passkey/register/complete', credential);

export const startPasskeyLogin = (email: string) =>
	client.post('/auth/passkey/login/start', { email });

export const completePasskeyLogin = (credential: any) =>
	client.post('/auth/passkey/login/complete', credential);

export interface User {
  id: string;
  email: string;
  name: string;
  authProvider: string;
  authId: string;
  roles: string[];
  createdAt: string;
  updatedAt: string;
}
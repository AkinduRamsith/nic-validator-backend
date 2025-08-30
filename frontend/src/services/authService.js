import { apiPost } from './apiManager';
import { API_ENDPOINTS } from '@/utils/constants';

export const authService = {
  login: async (credentials) => {
    return await apiPost(API_ENDPOINTS.LOGIN, credentials);
  },

  signup: async (userData) => {
    return await apiPost(API_ENDPOINTS.SIGNUP, userData);
  },

  forgotPassword: async (email) => {
    return await apiPost(API_ENDPOINTS.FORGOT_PASSWORD, { email });
  },

  resetPassword: async (resetData) => {
    return await apiPost(API_ENDPOINTS.RESET_PASSWORD, resetData);
  },

  refreshToken: async (refreshToken) => {
    return await apiPost(API_ENDPOINTS.REFRESH_TOKEN, { refreshToken });
  },

  logout: async () => {
    return await apiPost(API_ENDPOINTS.LOGOUT);
  },
};
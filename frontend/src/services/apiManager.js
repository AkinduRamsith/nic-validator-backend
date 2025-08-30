import axios from 'axios';
import { API_BASE_URL, STORAGE_KEYS } from '@/utils/constants';

// Create axios instance
const apiInstance = axios.create({
  baseURL: API_BASE_URL,
  timeout: 30000,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Token management
let isRefreshing = false;
let failedQueue = [];

const processQueue = (error, token = null) => {
  failedQueue.forEach(({ resolve, reject }) => {
    if (error) {
      reject(error);
    } else {
      resolve(token);
    }
  });
  
  failedQueue = [];
};

// Request interceptor to add auth token
apiInstance.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem(STORAGE_KEYS.AUTH_TOKEN);
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

// Response interceptor to handle token refresh
apiInstance.interceptors.response.use(
  (response) => response,
  async (error) => {
    const originalRequest = error.config;

    if (error.response?.status === 401 && !originalRequest._retry) {
      if (isRefreshing) {
        return new Promise((resolve, reject) => {
          failedQueue.push({ resolve, reject });
        }).then((token) => {
          originalRequest.headers.Authorization = `Bearer ${token}`;
          return apiInstance(originalRequest);
        }).catch((err) => {
          return Promise.reject(err);
        });
      }

      originalRequest._retry = true;
      isRefreshing = true;

      const refreshToken = localStorage.getItem(STORAGE_KEYS.REFRESH_TOKEN);
      
      if (!refreshToken) {
        processQueue(error, null);
        localStorage.removeItem(STORAGE_KEYS.AUTH_TOKEN);
        localStorage.removeItem(STORAGE_KEYS.REFRESH_TOKEN);
        localStorage.removeItem(STORAGE_KEYS.USER_DATA);
        window.location.href = '/auth/login';
        return Promise.reject(error);
      }

      try {
        const response = await axios.post(`${API_BASE_URL}/auth/refresh-token`, {
          refreshToken: refreshToken,
        });

        const { accessToken, refreshToken: newRefreshToken } = response.data.data;
        
        localStorage.setItem(STORAGE_KEYS.AUTH_TOKEN, accessToken);
        localStorage.setItem(STORAGE_KEYS.REFRESH_TOKEN, newRefreshToken);
        
        apiInstance.defaults.headers.common.Authorization = `Bearer ${accessToken}`;
        originalRequest.headers.Authorization = `Bearer ${accessToken}`;
        
        processQueue(null, accessToken);
        
        return apiInstance(originalRequest);
      } catch (refreshError) {
        processQueue(refreshError, null);
        localStorage.removeItem(STORAGE_KEYS.AUTH_TOKEN);
        localStorage.removeItem(STORAGE_KEYS.REFRESH_TOKEN);
        localStorage.removeItem(STORAGE_KEYS.USER_DATA);
        window.location.href = '/auth/login';
        return Promise.reject(refreshError);
      } finally {
        isRefreshing = false;
      }
    }

    return Promise.reject(error);
  }
);

// API methods
export const apiGet = async (url, config = {}) => {
  try {
    const response = await apiInstance.get(url, config);
    return response.data;
  } catch (error) {
    throw handleApiError(error);
  }
};

export const apiPost = async (url, data = {}, config = {}) => {
  try {
    const response = await apiInstance.post(url, data, config);
    return response.data;
  } catch (error) {
    throw handleApiError(error);
  }
};

export const apiPut = async (url, data = {}, config = {}) => {
  try {
    const response = await apiInstance.put(url, data, config);
    return response.data;
  } catch (error) {
    throw handleApiError(error);
  }
};

export const apiDelete = async (url, config = {}) => {
  try {
    const response = await apiInstance.delete(url, config);
    return response.data;
  } catch (error) {
    throw handleApiError(error);
  }
};

export const apiDownload = async (url, config = {}) => {
  try {
    const response = await apiInstance.get(url, {
      ...config,
      responseType: 'blob',
    });
    return response;
  } catch (error) {
    throw handleApiError(error);
  }
};

// Error handler
const handleApiError = (error) => {
  if (error.response) {
    // Server responded with error status
    const errorMessage = error.response.data?.error || 
                        error.response.data?.message || 
                        'An error occurred';
    return new Error(errorMessage);
  } else if (error.request) {
    // Network error
    return new Error('Network error. Please check your connection.');
  } else {
    // Other error
    return new Error(error.message || 'An unexpected error occurred');
  }
};

// Auth helpers
export const setAuthToken = (token) => {
  localStorage.setItem(STORAGE_KEYS.AUTH_TOKEN, token);
  apiInstance.defaults.headers.common.Authorization = `Bearer ${token}`;
};

export const removeAuthToken = () => {
  localStorage.removeItem(STORAGE_KEYS.AUTH_TOKEN);
  localStorage.removeItem(STORAGE_KEYS.REFRESH_TOKEN);
  localStorage.removeItem(STORAGE_KEYS.USER_DATA);
  delete apiInstance.defaults.headers.common.Authorization;
};

export default apiInstance;
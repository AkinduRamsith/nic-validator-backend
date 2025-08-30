// API Configuration
export const API_BASE_URL = process.env.NEXT_PUBLIC_API_BASE_URL || 'http://localhost:8080/api';

// Pricing Plans
export const PRICING_PLANS = {
  FREE: {
    name: 'Free',
    price: { monthly: 0, annual: 0 },
    features: {
      invoiceQuota: 25,
      pdfSizes: ['A4'],
      printLimit: 10,
      watermark: true,
      emailSending: false,
      smsSending: false,
      shareableLinks: true,
      editLimit: 5,
      support: 'Email only',
    },
    popular: false,
  },
  ELITE: {
    name: 'Elite',
    price: { monthly: 9, annual: 90 },
    features: {
      invoiceQuota: 100,
      pdfSizes: ['A4', 'Letter'],
      printLimit: 50,
      watermark: false,
      emailSending: true,
      smsSending: false,
      shareableLinks: true,
      editLimit: 20,
      support: 'Email + Ticket',
    },
    popular: false,
  },
  PRO: {
    name: 'Pro',
    price: { monthly: 29, annual: 290 },
    features: {
      invoiceQuota: 500,
      pdfSizes: ['A4', 'Letter', 'Legal'],
      printLimit: 'Unlimited',
      watermark: false,
      emailSending: true,
      smsSending: true,
      shareableLinks: true,
      editLimit: 100,
      support: 'Live chat + Priority email',
      paymentGateway: true,
    },
    popular: true,
  },
  ULTRA: {
    name: 'Ultra',
    price: { monthly: 79, annual: 790 },
    features: {
      invoiceQuota: 'Unlimited',
      pdfSizes: ['All sizes'],
      printLimit: 'Unlimited',
      watermark: false,
      emailSending: true,
      smsSending: true,
      shareableLinks: true,
      editLimit: 'Unlimited',
      support: 'Dedicated account manager + 24/7 chat',
      paymentGateway: true,
      multiUser: true,
      analytics: true,
      whiteLabel: true,
    },
    popular: false,
  },
};

// Routes
export const ROUTES = {
  HOME: '/',
  ABOUT: '/about',
  PRICING: '/pricing',
  CONTACT: '/contact',
  TERMS: '/terms',
  PRIVACY: '/privacy',
  LOGIN: '/auth/login',
  SIGNUP: '/auth/signup',
  FORGOT_PASSWORD: '/auth/forgot-password',
  RESET_PASSWORD: '/auth/reset-password',
  DASHBOARD: '/dashboard',
  ADMIN_DASHBOARD: '/dashboard/admin',
  CUSTOMER_DASHBOARD: '/dashboard/customer',
  INVOICES: '/dashboard/customer/invoices',
  CLIENTS: '/dashboard/customer/clients',
  PROJECTS: '/dashboard/customer/projects',
  ITEMS: '/dashboard/customer/items',
  SETTINGS: '/dashboard/customer/settings',
  ADMIN_CUSTOMERS: '/dashboard/admin/customers',
  ADMIN_PACKAGES: '/dashboard/admin/packages',
  ADMIN_SETTINGS: '/dashboard/admin/settings',
};

// User Roles
export const USER_ROLES = {
  ADMIN: 'ADMIN',
  CUSTOMER: 'CUSTOMER',
};

// Invoice Status
export const INVOICE_STATUS = {
  DRAFT: 'DRAFT',
  SENT: 'SENT',
  PAID: 'PAID',
  OVERDUE: 'OVERDUE',
  CANCELLED: 'CANCELLED',
};

// Local Storage Keys
export const STORAGE_KEYS = {
  AUTH_TOKEN: 'mint_invoiz_auth_token',
  REFRESH_TOKEN: 'mint_invoiz_refresh_token',
  USER_DATA: 'mint_invoiz_user_data',
  THEME_MODE: 'mint_invoiz_theme_mode',
};

// API Endpoints
export const API_ENDPOINTS = {
  // Auth
  LOGIN: '/auth/login',
  SIGNUP: '/auth/signup',
  FORGOT_PASSWORD: '/auth/forgot-password',
  RESET_PASSWORD: '/auth/reset-password',
  REFRESH_TOKEN: '/auth/refresh-token',
  LOGOUT: '/auth/logout',
  
  // Invoices
  INVOICES: '/invoices',
  INVOICE_PDF: (id) => `/invoices/${id}/pdf`,
  INVOICE_SHARE: (id) => `/invoices/${id}/share`,
  INVOICE_VIEW: (token) => `/invoices/view/${token}`,
  
  // Clients
  CLIENTS: '/clients',
  CLIENT_BY_ID: (id) => `/clients/${id}`,
  
  // Projects
  PROJECTS: '/projects',
  PROJECT_BY_ID: (id) => `/projects/${id}`,
  
  // Items
  ITEMS: '/items',
  ITEM_BY_ID: (id) => `/items/${id}`,
  
  // Admin
  ADMIN_CUSTOMERS: '/admin/customers',
  ADMIN_PACKAGES: '/admin/packages',
  ADMIN_SETTINGS: '/admin/settings',
  ADMIN_ANALYTICS: '/admin/analytics',
};

// Form Validation Rules
export const VALIDATION_RULES = {
  EMAIL: /^[^\s@]+@[^\s@]+\.[^\s@]+$/,
  PHONE: /^[\+]?[1-9][\d]{0,15}$/,
  PASSWORD_MIN_LENGTH: 8,
};

// Company Information
export const COMPANY_INFO = {
  name: 'Mint Invoiz',
  tagline: 'Fresh invoices, minted for success.',
  parent: 'OXY12',
  domain: 'mintinvoiz.com',
  email: 'hello@mintinvoiz.com',
  phone: '+1 (555) 123-4567',
  address: '123 Business Street, Suite 100, City, State 12345',
};
import { apiGet, apiPost, apiDownload } from './apiManager';
import { API_ENDPOINTS } from '@/utils/constants';

export const invoiceService = {
  createInvoice: async (invoiceData) => {
    return await apiPost(API_ENDPOINTS.INVOICES, invoiceData);
  },

  getInvoices: async (params = {}) => {
    const searchParams = new URLSearchParams(params);
    return await apiGet(`${API_ENDPOINTS.INVOICES}?${searchParams}`);
  },

  getInvoiceById: async (id) => {
    return await apiGet(`${API_ENDPOINTS.INVOICES}/${id}`);
  },

  downloadInvoicePdf: async (id) => {
    return await apiDownload(API_ENDPOINTS.INVOICE_PDF(id));
  },

  shareInvoice: async (id, shareData) => {
    return await apiPost(API_ENDPOINTS.INVOICE_SHARE(id), shareData);
  },

  getSharedInvoice: async (token) => {
    return await apiGet(API_ENDPOINTS.INVOICE_VIEW(token));
  },
};
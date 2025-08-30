import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { invoiceService } from '@/services/invoiceService';
import { useDispatch } from 'react-redux';
import { addNotification } from '@/redux/reducers/ui.reducer';

export const useInvoices = (params = {}) => {
  const queryClient = useQueryClient();
  const dispatch = useDispatch();

  // Get invoices query
  const {
    data: invoicesData,
    isLoading: isLoadingInvoices,
    error: invoicesError,
    refetch: refetchInvoices,
  } = useQuery({
    queryKey: ['invoices', params],
    queryFn: () => invoiceService.getInvoices(params),
    staleTime: 5 * 60 * 1000, // 5 minutes
  });

  // Create invoice mutation
  const createInvoiceMutation = useMutation({
    mutationFn: invoiceService.createInvoice,
    onSuccess: (data) => {
      queryClient.invalidateQueries({ queryKey: ['invoices'] });
      dispatch(addNotification({
        type: 'success',
        message: `Invoice ${data.data.invoiceNumber} created successfully!`,
      }));
    },
    onError: (error) => {
      dispatch(addNotification({
        type: 'error',
        message: error.message || 'Failed to create invoice',
      }));
    },
  });

  // Share invoice mutation
  const shareInvoiceMutation = useMutation({
    mutationFn: ({ id, shareData }) => invoiceService.shareInvoice(id, shareData),
    onSuccess: (data) => {
      dispatch(addNotification({
        type: 'success',
        message: data.data.message || 'Invoice shared successfully!',
      }));
    },
    onError: (error) => {
      dispatch(addNotification({
        type: 'error',
        message: error.message || 'Failed to share invoice',
      }));
    },
  });

  // Download PDF function
  const downloadPdf = async (invoiceId, invoiceNumber) => {
    try {
      const response = await invoiceService.downloadInvoicePdf(invoiceId);
      
      // Create blob and download
      const blob = new Blob([response.data], { type: 'application/pdf' });
      const url = window.URL.createObjectURL(blob);
      const link = document.createElement('a');
      link.href = url;
      link.download = `invoice-${invoiceNumber || invoiceId}.pdf`;
      document.body.appendChild(link);
      link.click();
      document.body.removeChild(link);
      window.URL.revokeObjectURL(url);
      
      dispatch(addNotification({
        type: 'success',
        message: 'PDF downloaded successfully!',
      }));
    } catch (error) {
      dispatch(addNotification({
        type: 'error',
        message: error.message || 'Failed to download PDF',
      }));
    }
  };

  return {
    // Data
    invoices: invoicesData?.data?.content || [],
    totalPages: invoicesData?.data?.totalPages || 0,
    totalElements: invoicesData?.data?.totalElements || 0,
    
    // Loading states
    isLoadingInvoices,
    isCreatingInvoice: createInvoiceMutation.isPending,
    isSharingInvoice: shareInvoiceMutation.isPending,
    
    // Errors
    invoicesError,
    createError: createInvoiceMutation.error?.message,
    shareError: shareInvoiceMutation.error?.message,
    
    // Actions
    refetchInvoices,
    createInvoice: createInvoiceMutation.mutate,
    shareInvoice: shareInvoiceMutation.mutate,
    downloadPdf,
    
    // Mutations for direct access
    createInvoiceMutation,
    shareInvoiceMutation,
  };
};

export const useInvoice = (invoiceId) => {
  return useQuery({
    queryKey: ['invoice', invoiceId],
    queryFn: () => invoiceService.getInvoiceById(invoiceId),
    enabled: !!invoiceId,
    staleTime: 5 * 60 * 1000,
  });
};

export const useSharedInvoice = (shareToken) => {
  return useQuery({
    queryKey: ['shared-invoice', shareToken],
    queryFn: () => invoiceService.getSharedInvoice(shareToken),
    enabled: !!shareToken,
    staleTime: 30 * 60 * 1000, // 30 minutes for shared invoices
  });
};
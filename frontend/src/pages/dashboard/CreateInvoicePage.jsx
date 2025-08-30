'use client';

import { useState } from 'react';
import {
  Box,
  Container,
  Card,
  CardContent,
  Typography,
  TextField,
  Button,
  Grid,
  MenuItem,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Paper,
  IconButton,
  Divider,
  Alert,
} from '@mui/material';
import {
  Add as AddIcon,
  Delete as DeleteIcon,
  Save as SaveIcon,
  Preview as PreviewIcon,
} from '@mui/icons-material';
import { DatePicker } from '@mui/x-date-pickers/DatePicker';
import { LocalizationProvider } from '@mui/x-date-pickers/LocalizationProvider';
import { AdapterDateFns } from '@mui/x-date-pickers/AdapterDateFns';
import ProtectedRoute from '@/components/common/ProtectedRoute';
import MainLayout from '@/components/layout/MainLayout';
import { useInvoices } from '@/hooks/useInvoices';

const CreateInvoicePage = () => {
  const [formData, setFormData] = useState({
    clientId: '',
    projectId: '',
    invoiceDate: new Date(),
    dueDate: null,
    notes: '',
    termsConditions: '',
    paymentInstructions: '',
  });

  const [invoiceItems, setInvoiceItems] = useState([
    {
      itemName: '',
      itemDescription: '',
      quantity: 1,
      unitPrice: 0,
      taxRate: 0,
      discountType: 'NONE',
      discountValue: 0,
    },
  ]);

  const { createInvoice, isCreatingInvoice } = useInvoices();

  const handleFormChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value,
    }));
  };

  const handleItemChange = (index, field, value) => {
    const updatedItems = [...invoiceItems];
    updatedItems[index] = {
      ...updatedItems[index],
      [field]: value,
    };
    setInvoiceItems(updatedItems);
  };

  const addInvoiceItem = () => {
    setInvoiceItems([
      ...invoiceItems,
      {
        itemName: '',
        itemDescription: '',
        quantity: 1,
        unitPrice: 0,
        taxRate: 0,
        discountType: 'NONE',
        discountValue: 0,
      },
    ]);
  };

  const removeInvoiceItem = (index) => {
    if (invoiceItems.length > 1) {
      setInvoiceItems(invoiceItems.filter((_, i) => i !== index));
    }
  };

  const calculateLineTotal = (item) => {
    const subtotal = item.quantity * item.unitPrice;
    let discount = 0;
    
    if (item.discountType === 'PERCENTAGE') {
      discount = subtotal * (item.discountValue / 100);
    } else if (item.discountType === 'FIXED_AMOUNT') {
      discount = Math.min(item.discountValue, subtotal);
    }
    
    const afterDiscount = subtotal - discount;
    const tax = afterDiscount * (item.taxRate / 100);
    
    return afterDiscount + tax;
  };

  const calculateTotals = () => {
    const subtotal = invoiceItems.reduce((sum, item) => sum + (item.quantity * item.unitPrice), 0);
    const totalDiscount = invoiceItems.reduce((sum, item) => {
      const itemSubtotal = item.quantity * item.unitPrice;
      if (item.discountType === 'PERCENTAGE') {
        return sum + (itemSubtotal * (item.discountValue / 100));
      } else if (item.discountType === 'FIXED_AMOUNT') {
        return sum + Math.min(item.discountValue, itemSubtotal);
      }
      return sum;
    }, 0);
    
    const afterDiscount = subtotal - totalDiscount;
    const totalTax = invoiceItems.reduce((sum, item) => {
      const itemSubtotal = item.quantity * item.unitPrice;
      let itemDiscount = 0;
      if (item.discountType === 'PERCENTAGE') {
        itemDiscount = itemSubtotal * (item.discountValue / 100);
      } else if (item.discountType === 'FIXED_AMOUNT') {
        itemDiscount = Math.min(item.discountValue, itemSubtotal);
      }
      const itemAfterDiscount = itemSubtotal - itemDiscount;
      return sum + (itemAfterDiscount * (item.taxRate / 100));
    }, 0);
    
    const grandTotal = afterDiscount + totalTax;
    
    return { subtotal, totalDiscount, totalTax, grandTotal };
  };

  const totals = calculateTotals();

  const handleSubmit = (e) => {
    e.preventDefault();
    
    const invoiceData = {
      ...formData,
      items: invoiceItems.map((item, index) => ({
        ...item,
        sortOrder: index,
      })),
    };
    
    createInvoice(invoiceData);
  };

  return (
    <ProtectedRoute requiredRole="CUSTOMER">
      <MainLayout>
        <LocalizationProvider dateAdapter={AdapterDateFns}>
          <Box sx={{ flexGrow: 1, py: 4, backgroundColor: 'grey.50' }}>
            <Container maxWidth="lg">
              <Typography
                variant="h4"
                component="h1"
                sx={{
                  fontWeight: 600,
                  mb: 4,
                  color: 'text.primary',
                }}
              >
                Create New Invoice
              </Typography>

              <Box component="form" onSubmit={handleSubmit}>
                {/* Invoice Details */}
                <Card sx={{ mb: 4 }}>
                  <CardContent>
                    <Typography variant="h6" sx={{ mb: 3, fontWeight: 600 }}>
                      Invoice Details
                    </Typography>
                    
                    <Grid container spacing={3}>
                      <Grid item xs={12} md={6}>
                        <TextField
                          fullWidth
                          select
                          name="clientId"
                          label="Select Client"
                          value={formData.clientId}
                          onChange={handleFormChange}
                          required
                        >
                          <MenuItem value="">Select a client...</MenuItem>
                          {/* TODO: Load clients from API */}
                          <MenuItem value="1">Sample Client 1</MenuItem>
                          <MenuItem value="2">Sample Client 2</MenuItem>
                        </TextField>
                      </Grid>
                      
                      <Grid item xs={12} md={6}>
                        <TextField
                          fullWidth
                          select
                          name="projectId"
                          label="Select Project (Optional)"
                          value={formData.projectId}
                          onChange={handleFormChange}
                        >
                          <MenuItem value="">No project</MenuItem>
                          {/* TODO: Load projects from API */}
                          <MenuItem value="1">Sample Project 1</MenuItem>
                          <MenuItem value="2">Sample Project 2</MenuItem>
                        </TextField>
                      </Grid>
                      
                      <Grid item xs={12} md={6}>
                        <DatePicker
                          label="Invoice Date"
                          value={formData.invoiceDate}
                          onChange={(newValue) => setFormData(prev => ({ ...prev, invoiceDate: newValue }))}
                          renderInput={(params) => <TextField {...params} fullWidth required />}
                        />
                      </Grid>
                      
                      <Grid item xs={12} md={6}>
                        <DatePicker
                          label="Due Date (Optional)"
                          value={formData.dueDate}
                          onChange={(newValue) => setFormData(prev => ({ ...prev, dueDate: newValue }))}
                          renderInput={(params) => <TextField {...params} fullWidth />}
                        />
                      </Grid>
                    </Grid>
                  </CardContent>
                </Card>

                {/* Invoice Items */}
                <Card sx={{ mb: 4 }}>
                  <CardContent>
                    <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 3 }}>
                      <Typography variant="h6" sx={{ fontWeight: 600 }}>
                        Invoice Items
                      </Typography>
                      <Button
                        startIcon={<AddIcon />}
                        onClick={addInvoiceItem}
                        variant="outlined"
                      >
                        Add Item
                      </Button>
                    </Box>

                    <TableContainer component={Paper} variant="outlined">
                      <Table>
                        <TableHead>
                          <TableRow>
                            <TableCell>Item</TableCell>
                            <TableCell>Qty</TableCell>
                            <TableCell>Unit Price</TableCell>
                            <TableCell>Tax %</TableCell>
                            <TableCell>Discount</TableCell>
                            <TableCell>Total</TableCell>
                            <TableCell>Action</TableCell>
                          </TableRow>
                        </TableHead>
                        <TableBody>
                          {invoiceItems.map((item, index) => (
                            <TableRow key={index}>
                              <TableCell>
                                <TextField
                                  fullWidth
                                  size="small"
                                  placeholder="Item name"
                                  value={item.itemName}
                                  onChange={(e) => handleItemChange(index, 'itemName', e.target.value)}
                                  required
                                />
                              </TableCell>
                              <TableCell>
                                <TextField
                                  size="small"
                                  type="number"
                                  value={item.quantity}
                                  onChange={(e) => handleItemChange(index, 'quantity', parseFloat(e.target.value) || 0)}
                                  inputProps={{ min: 0, step: 0.01 }}
                                  sx={{ width: 80 }}
                                />
                              </TableCell>
                              <TableCell>
                                <TextField
                                  size="small"
                                  type="number"
                                  value={item.unitPrice}
                                  onChange={(e) => handleItemChange(index, 'unitPrice', parseFloat(e.target.value) || 0)}
                                  inputProps={{ min: 0, step: 0.01 }}
                                  sx={{ width: 100 }}
                                />
                              </TableCell>
                              <TableCell>
                                <TextField
                                  size="small"
                                  type="number"
                                  value={item.taxRate}
                                  onChange={(e) => handleItemChange(index, 'taxRate', parseFloat(e.target.value) || 0)}
                                  inputProps={{ min: 0, max: 100, step: 0.01 }}
                                  sx={{ width: 80 }}
                                />
                              </TableCell>
                              <TableCell>
                                <TextField
                                  size="small"
                                  select
                                  value={item.discountType}
                                  onChange={(e) => handleItemChange(index, 'discountType', e.target.value)}
                                  sx={{ width: 100 }}
                                >
                                  <MenuItem value="NONE">None</MenuItem>
                                  <MenuItem value="PERCENTAGE">%</MenuItem>
                                  <MenuItem value="FIXED_AMOUNT">$</MenuItem>
                                </TextField>
                                {item.discountType !== 'NONE' && (
                                  <TextField
                                    size="small"
                                    type="number"
                                    value={item.discountValue}
                                    onChange={(e) => handleItemChange(index, 'discountValue', parseFloat(e.target.value) || 0)}
                                    inputProps={{ min: 0, step: 0.01 }}
                                    sx={{ width: 80, ml: 1 }}
                                  />
                                )}
                              </TableCell>
                              <TableCell>
                                <Typography variant="body2" sx={{ fontWeight: 600 }}>
                                  ${calculateLineTotal(item).toFixed(2)}
                                </Typography>
                              </TableCell>
                              <TableCell>
                                <IconButton
                                  onClick={() => removeInvoiceItem(index)}
                                  disabled={invoiceItems.length === 1}
                                  color="error"
                                  size="small"
                                >
                                  <DeleteIcon />
                                </IconButton>
                              </TableCell>
                            </TableRow>
                          ))}
                        </TableBody>
                      </Table>
                    </TableContainer>

                    {/* Totals */}
                    <Box sx={{ mt: 3, display: 'flex', justifyContent: 'flex-end' }}>
                      <Box sx={{ minWidth: 300 }}>
                        <Box sx={{ display: 'flex', justifyContent: 'space-between', mb: 1 }}>
                          <Typography>Subtotal:</Typography>
                          <Typography>${totals.subtotal.toFixed(2)}</Typography>
                        </Box>
                        <Box sx={{ display: 'flex', justifyContent: 'space-between', mb: 1 }}>
                          <Typography>Discount:</Typography>
                          <Typography>-${totals.totalDiscount.toFixed(2)}</Typography>
                        </Box>
                        <Box sx={{ display: 'flex', justifyContent: 'space-between', mb: 1 }}>
                          <Typography>Tax:</Typography>
                          <Typography>${totals.totalTax.toFixed(2)}</Typography>
                        </Box>
                        <Divider sx={{ my: 1 }} />
                        <Box sx={{ display: 'flex', justifyContent: 'space-between' }}>
                          <Typography variant="h6" sx={{ fontWeight: 600 }}>
                            Total:
                          </Typography>
                          <Typography variant="h6" sx={{ fontWeight: 600, color: 'primary.main' }}>
                            ${totals.grandTotal.toFixed(2)}
                          </Typography>
                        </Box>
                      </Box>
                    </Box>
                  </CardContent>
                </Card>

                {/* Additional Information */}
                <Card sx={{ mb: 4 }}>
                  <CardContent>
                    <Typography variant="h6" sx={{ mb: 3, fontWeight: 600 }}>
                      Additional Information
                    </Typography>
                    
                    <Grid container spacing={3}>
                      <Grid item xs={12}>
                        <TextField
                          fullWidth
                          multiline
                          rows={3}
                          name="notes"
                          label="Notes (Optional)"
                          value={formData.notes}
                          onChange={handleFormChange}
                          placeholder="Add any additional notes for this invoice..."
                        />
                      </Grid>
                      
                      <Grid item xs={12} md={6}>
                        <TextField
                          fullWidth
                          multiline
                          rows={3}
                          name="termsConditions"
                          label="Terms & Conditions (Optional)"
                          value={formData.termsConditions}
                          onChange={handleFormChange}
                          placeholder="Payment terms, late fees, etc..."
                        />
                      </Grid>
                      
                      <Grid item xs={12} md={6}>
                        <TextField
                          fullWidth
                          multiline
                          rows={3}
                          name="paymentInstructions"
                          label="Payment Instructions (Optional)"
                          value={formData.paymentInstructions}
                          onChange={handleFormChange}
                          placeholder="Bank details, payment methods, etc..."
                        />
                      </Grid>
                    </Grid>
                  </CardContent>
                </Card>

                {/* Action Buttons */}
                <Box sx={{ display: 'flex', gap: 2, justifyContent: 'flex-end' }}>
                  <Button
                    variant="outlined"
                    startIcon={<PreviewIcon />}
                    size="large"
                  >
                    Preview
                  </Button>
                  <Button
                    type="submit"
                    variant="contained"
                    startIcon={<SaveIcon />}
                    size="large"
                    disabled={isCreatingInvoice}
                  >
                    {isCreatingInvoice ? 'Creating...' : 'Create Invoice'}
                  </Button>
                </Box>
              </Box>
            </Container>
          </Box>
        </LocalizationProvider>
      </MainLayout>
    </ProtectedRoute>
  );
};

export default CreateInvoicePage;
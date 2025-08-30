'use client';

import {
  Box,
  Container,
  Grid,
  Card,
  CardContent,
  Typography,
  Button,
  Stack,
  LinearProgress,
  Chip,
} from '@mui/material';
import {
  Receipt as ReceiptIcon,
  People as PeopleIcon,
  Work as WorkIcon,
  TrendingUp as TrendingUpIcon,
  Add as AddIcon,
} from '@mui/icons-material';
import Link from 'next/link';
import ProtectedRoute from '@/components/common/ProtectedRoute';
import MainLayout from '@/components/layout/MainLayout';
import { useSelector } from 'react-redux';
import { selectUser } from '@/redux/reducers/auth.reducer';
import { ROUTES, PRICING_PLANS } from '@/utils/constants';

const CustomerDashboard = () => {
  const user = useSelector(selectUser);
  const subscription = user?.subscription;
  
  // Mock data - in real app, fetch from API
  const stats = {
    totalInvoices: 12,
    totalClients: 8,
    totalProjects: 5,
    monthlyRevenue: 2450,
  };

  const getQuotaInfo = () => {
    if (!subscription) return null;
    
    const plan = PRICING_PLANS[subscription.packageType];
    if (!plan) return null;
    
    const invoiceQuota = plan.features.invoiceQuota;
    const used = subscription.invoiceQuotaUsed || 0;
    
    return {
      quota: invoiceQuota === 'Unlimited' ? -1 : invoiceQuota,
      used,
      percentage: invoiceQuota === 'Unlimited' ? 0 : (used / invoiceQuota) * 100,
    };
  };

  const quotaInfo = getQuotaInfo();

  return (
    <ProtectedRoute requiredRole="CUSTOMER">
      <MainLayout>
        <Box sx={{ flexGrow: 1, py: 4, backgroundColor: 'grey.50' }}>
          <Container maxWidth="lg">
            {/* Welcome Section */}
            <Box sx={{ mb: 4 }}>
              <Typography
                variant="h4"
                component="h1"
                sx={{
                  fontWeight: 600,
                  mb: 1,
                  color: 'text.primary',
                }}
              >
                Welcome back, {user?.firstName}!
              </Typography>
              <Typography
                variant="body1"
                sx={{ color: 'text.secondary' }}
              >
                Here's what's happening with your invoices today.
              </Typography>
            </Box>

            {/* Quick Actions */}
            <Box sx={{ mb: 4 }}>
              <Stack direction={{ xs: 'column', sm: 'row' }} spacing={2}>
                <Button
                  component={Link}
                  href={ROUTES.INVOICES + '/create'}
                  variant="contained"
                  startIcon={<AddIcon />}
                  size="large"
                  sx={{ fontWeight: 600 }}
                >
                  Create Invoice
                </Button>
                <Button
                  component={Link}
                  href={ROUTES.CLIENTS + '/create'}
                  variant="outlined"
                  startIcon={<PeopleIcon />}
                  size="large"
                >
                  Add Client
                </Button>
                <Button
                  component={Link}
                  href={ROUTES.PROJECTS + '/create'}
                  variant="outlined"
                  startIcon={<WorkIcon />}
                  size="large"
                >
                  New Project
                </Button>
              </Stack>
            </Box>

            {/* Stats Cards */}
            <Grid container spacing={3} sx={{ mb: 4 }}>
              <Grid item xs={12} sm={6} md={3}>
                <Card>
                  <CardContent>
                    <Box sx={{ display: 'flex', alignItems: 'center', mb: 2 }}>
                      <ReceiptIcon sx={{ color: 'primary.main', mr: 1 }} />
                      <Typography variant="h6" sx={{ fontWeight: 600 }}>
                        Invoices
                      </Typography>
                    </Box>
                    <Typography variant="h4" sx={{ fontWeight: 700, color: 'primary.main' }}>
                      {stats.totalInvoices}
                    </Typography>
                    <Typography variant="body2" color="text.secondary">
                      Total invoices created
                    </Typography>
                  </CardContent>
                </Card>
              </Grid>

              <Grid item xs={12} sm={6} md={3}>
                <Card>
                  <CardContent>
                    <Box sx={{ display: 'flex', alignItems: 'center', mb: 2 }}>
                      <PeopleIcon sx={{ color: 'success.main', mr: 1 }} />
                      <Typography variant="h6" sx={{ fontWeight: 600 }}>
                        Clients
                      </Typography>
                    </Box>
                    <Typography variant="h4" sx={{ fontWeight: 700, color: 'success.main' }}>
                      {stats.totalClients}
                    </Typography>
                    <Typography variant="body2" color="text.secondary">
                      Active clients
                    </Typography>
                  </CardContent>
                </Card>
              </Grid>

              <Grid item xs={12} sm={6} md={3}>
                <Card>
                  <CardContent>
                    <Box sx={{ display: 'flex', alignItems: 'center', mb: 2 }}>
                      <WorkIcon sx={{ color: 'warning.main', mr: 1 }} />
                      <Typography variant="h6" sx={{ fontWeight: 600 }}>
                        Projects
                      </Typography>
                    </Box>
                    <Typography variant="h4" sx={{ fontWeight: 700, color: 'warning.main' }}>
                      {stats.totalProjects}
                    </Typography>
                    <Typography variant="body2" color="text.secondary">
                      Active projects
                    </Typography>
                  </CardContent>
                </Card>
              </Grid>

              <Grid item xs={12} sm={6} md={3}>
                <Card>
                  <CardContent>
                    <Box sx={{ display: 'flex', alignItems: 'center', mb: 2 }}>
                      <TrendingUpIcon sx={{ color: 'info.main', mr: 1 }} />
                      <Typography variant="h6" sx={{ fontWeight: 600 }}>
                        Revenue
                      </Typography>
                    </Box>
                    <Typography variant="h4" sx={{ fontWeight: 700, color: 'info.main' }}>
                      ${stats.monthlyRevenue}
                    </Typography>
                    <Typography variant="body2" color="text.secondary">
                      This month
                    </Typography>
                  </CardContent>
                </Card>
              </Grid>
            </Grid>

            {/* Subscription Status */}
            {quotaInfo && (
              <Card sx={{ mb: 4 }}>
                <CardContent>
                  <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 2 }}>
                    <Typography variant="h6" sx={{ fontWeight: 600 }}>
                      Subscription Status
                    </Typography>
                    <Chip
                      label={subscription?.packageType || 'FREE'}
                      color="primary"
                      variant="outlined"
                    />
                  </Box>
                  
                  <Typography variant="body2" color="text.secondary" sx={{ mb: 2 }}>
                    Invoice Usage This Month
                  </Typography>
                  
                  {quotaInfo.quota === -1 ? (
                    <Typography variant="body1">
                      Unlimited invoices
                    </Typography>
                  ) : (
                    <Box>
                      <Box sx={{ display: 'flex', justifyContent: 'space-between', mb: 1 }}>
                        <Typography variant="body2">
                          {quotaInfo.used} of {quotaInfo.quota} invoices used
                        </Typography>
                        <Typography variant="body2">
                          {Math.round(quotaInfo.percentage)}%
                        </Typography>
                      </Box>
                      <LinearProgress
                        variant="determinate"
                        value={quotaInfo.percentage}
                        sx={{ height: 8, borderRadius: 4 }}
                      />
                      
                      {quotaInfo.percentage > 80 && (
                        <Box sx={{ mt: 2 }}>
                          <Typography variant="body2" color="warning.main" sx={{ mb: 1 }}>
                            You're approaching your monthly limit!
                          </Typography>
                          <Button
                            component={Link}
                            href={ROUTES.PRICING}
                            variant="outlined"
                            size="small"
                            color="primary"
                          >
                            Upgrade Plan
                          </Button>
                        </Box>
                      )}
                    </Box>
                  )}
                </CardContent>
              </Card>
            )}

            {/* Recent Activity */}
            <Card>
              <CardContent>
                <Typography variant="h6" sx={{ fontWeight: 600, mb: 3 }}>
                  Recent Activity
                </Typography>
                
                <Typography variant="body2" color="text.secondary" sx={{ textAlign: 'center', py: 4 }}>
                  No recent activity to display.
                  <br />
                  <Button
                    component={Link}
                    href={ROUTES.INVOICES + '/create'}
                    variant="contained"
                    startIcon={<AddIcon />}
                    sx={{ mt: 2 }}
                  >
                    Create Your First Invoice
                  </Button>
                </Typography>
              </CardContent>
            </Card>
          </Container>
        </Box>
      </MainLayout>
    </ProtectedRoute>
  );
};

export default CustomerDashboard;
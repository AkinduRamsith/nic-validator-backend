'use client';

import { useEffect } from 'react';
import { useSelector } from 'react-redux';
import { useRouter } from 'next/navigation';
import { Box, CircularProgress } from '@mui/material';
import { selectIsAuthenticated, selectUser, selectAuthLoading } from '@/redux/reducers/auth.reducer';
import { ROUTES } from '@/utils/constants';

const ProtectedRoute = ({ children, requiredRole = null, redirectTo = ROUTES.LOGIN }) => {
  const router = useRouter();
  const isAuthenticated = useSelector(selectIsAuthenticated);
  const user = useSelector(selectUser);
  const isLoading = useSelector(selectAuthLoading);

  useEffect(() => {
    // Wait for auth state to be loaded from persistence
    if (!isLoading) {
      if (!isAuthenticated) {
        router.push(redirectTo);
        return;
      }

      // Check role if required
      if (requiredRole && user?.role !== requiredRole) {
        if (user?.role === 'ADMIN') {
          router.push(ROUTES.ADMIN_DASHBOARD);
        } else if (user?.role === 'CUSTOMER') {
          router.push(ROUTES.CUSTOMER_DASHBOARD);
        } else {
          router.push(ROUTES.HOME);
        }
        return;
      }
    }
  }, [isAuthenticated, user, isLoading, requiredRole, router, redirectTo]);

  // Show loading while checking auth state
  if (isLoading) {
    return (
      <Box
        sx={{
          display: 'flex',
          justifyContent: 'center',
          alignItems: 'center',
          height: '100vh',
        }}
      >
        <CircularProgress />
      </Box>
    );
  }

  // Show nothing while redirecting
  if (!isAuthenticated || (requiredRole && user?.role !== requiredRole)) {
    return (
      <Box
        sx={{
          display: 'flex',
          justifyContent: 'center',
          alignItems: 'center',
          height: '100vh',
        }}
      >
        <CircularProgress />
      </Box>
    );
  }

  return children;
};

export default ProtectedRoute;
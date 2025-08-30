'use client';

import { useEffect } from 'react';
import { useSelector } from 'react-redux';
import { useRouter } from 'next/navigation';
import { Box, CircularProgress } from '@mui/material';
import { selectIsAuthenticated, selectUser, selectAuthLoading } from '@/redux/reducers/auth.reducer';
import { ROUTES } from '@/utils/constants';

const GuestGuard = ({ children }) => {
  const router = useRouter();
  const isAuthenticated = useSelector(selectIsAuthenticated);
  const user = useSelector(selectUser);
  const isLoading = useSelector(selectAuthLoading);

  useEffect(() => {
    // Wait for auth state to be loaded from persistence
    if (!isLoading && isAuthenticated) {
      // Redirect authenticated users to their dashboard
      if (user?.role === 'ADMIN') {
        router.push(ROUTES.ADMIN_DASHBOARD);
      } else if (user?.role === 'CUSTOMER') {
        router.push(ROUTES.CUSTOMER_DASHBOARD);
      } else {
        router.push(ROUTES.HOME);
      }
    }
  }, [isAuthenticated, user, isLoading, router]);

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

  // Show nothing while redirecting authenticated users
  if (isAuthenticated) {
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

export default GuestGuard;
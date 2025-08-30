'use client';

import { Box } from '@mui/material';
import Header from '@/components/common/Header';
import Footer from '@/components/common/Footer';

const MainLayout = ({ children }) => {
  return (
    <Box
      sx={{
        display: 'flex',
        flexDirection: 'column',
        minHeight: '100vh',
      }}
    >
      <Header />
      
      <Box
        component="main"
        sx={{
          flexGrow: 1,
          display: 'flex',
          flexDirection: 'column',
        }}
      >
        {children}
      </Box>
      
      <Footer />
    </Box>
  );
};

export default MainLayout;
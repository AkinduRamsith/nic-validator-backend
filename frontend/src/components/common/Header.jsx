'use client';

import { useState } from 'react';
import {
  AppBar,
  Toolbar,
  Typography,
  Button,
  Box,
  IconButton,
  Drawer,
  List,
  ListItem,
  ListItemText,
  useMediaQuery,
  useTheme,
  Container,
} from '@mui/material';
import {
  Menu as MenuIcon,
  Close as CloseIcon,
} from '@mui/icons-material';
import Link from 'next/link';
import { useRouter } from 'next/navigation';
import { useAuth } from '@/hooks/useAuth';
import { ROUTES } from '@/utils/constants';

const Header = () => {
  const theme = useTheme();
  const isMobile = useMediaQuery(theme.breakpoints.down('md'));
  const [mobileMenuOpen, setMobileMenuOpen] = useState(false);
  const router = useRouter();
  const { isAuthenticated, user, logout } = useAuth();

  const navigationItems = [
    { label: 'Home', href: ROUTES.HOME },
    { label: 'About', href: ROUTES.ABOUT },
    { label: 'Pricing', href: ROUTES.PRICING },
    { label: 'Contact', href: ROUTES.CONTACT },
  ];

  const handleMobileMenuToggle = () => {
    setMobileMenuOpen(!mobileMenuOpen);
  };

  const handleNavigation = (href) => {
    router.push(href);
    setMobileMenuOpen(false);
  };

  const handleLogout = () => {
    logout();
    setMobileMenuOpen(false);
  };

  const renderMobileMenu = () => (
    <Drawer
      anchor="right"
      open={mobileMenuOpen}
      onClose={handleMobileMenuToggle}
      PaperProps={{
        sx: { width: 280, pt: 2 },
      }}
    >
      <Box sx={{ display: 'flex', justifyContent: 'flex-end', px: 2, mb: 2 }}>
        <IconButton onClick={handleMobileMenuToggle}>
          <CloseIcon />
        </IconButton>
      </Box>
      
      <List>
        {navigationItems.map((item) => (
          <ListItem 
            key={item.label}
            onClick={() => handleNavigation(item.href)}
            sx={{ cursor: 'pointer' }}
          >
            <ListItemText primary={item.label} />
          </ListItem>
        ))}
        
        {isAuthenticated ? (
          <>
            <ListItem 
              onClick={() => handleNavigation(user?.role === 'ADMIN' ? ROUTES.ADMIN_DASHBOARD : ROUTES.CUSTOMER_DASHBOARD)}
              sx={{ cursor: 'pointer' }}
            >
              <ListItemText primary="Dashboard" />
            </ListItem>
            <ListItem onClick={handleLogout} sx={{ cursor: 'pointer' }}>
              <ListItemText primary="Logout" />
            </ListItem>
          </>
        ) : (
          <>
            <ListItem onClick={() => handleNavigation(ROUTES.LOGIN)} sx={{ cursor: 'pointer' }}>
              <ListItemText primary="Login" />
            </ListItem>
            <ListItem onClick={() => handleNavigation(ROUTES.SIGNUP)} sx={{ cursor: 'pointer' }}>
              <ListItemText primary="Get Started" />
            </ListItem>
          </>
        )}
      </List>
    </Drawer>
  );

  return (
    <>
      <AppBar 
        position="sticky" 
        color="default" 
        elevation={1}
        sx={{ 
          backgroundColor: 'white',
          borderBottom: '1px solid',
          borderBottomColor: 'divider',
        }}
      >
        <Container maxWidth="lg">
          <Toolbar sx={{ px: { xs: 0, sm: 0 } }}>
            {/* Logo */}
            <Box 
              component={Link} 
              href={ROUTES.HOME}
              sx={{ 
                display: 'flex', 
                alignItems: 'center', 
                textDecoration: 'none',
                color: 'inherit',
                flexGrow: isMobile ? 1 : 0,
              }}
            >
              <Typography
                variant="h5"
                component="div"
                sx={{
                  fontWeight: 700,
                  color: 'primary.main',
                  fontSize: { xs: '1.5rem', sm: '1.75rem' },
                }}
              >
                Mint Invoiz
              </Typography>
            </Box>

            {/* Desktop Navigation */}
            {!isMobile && (
              <Box sx={{ flexGrow: 1, display: 'flex', justifyContent: 'center', ml: 4 }}>
                {navigationItems.map((item) => (
                  <Button
                    key={item.label}
                    component={Link}
                    href={item.href}
                    color="inherit"
                    sx={{ 
                      mx: 1,
                      color: 'text.primary',
                      '&:hover': {
                        backgroundColor: 'primary.light',
                        color: 'white',
                      },
                    }}
                  >
                    {item.label}
                  </Button>
                ))}
              </Box>
            )}

            {/* Desktop Auth Buttons */}
            {!isMobile && (
              <Box sx={{ display: 'flex', gap: 1 }}>
                {isAuthenticated ? (
                  <>
                    <Button
                      component={Link}
                      href={user?.role === 'ADMIN' ? ROUTES.ADMIN_DASHBOARD : ROUTES.CUSTOMER_DASHBOARD}
                      color="inherit"
                      sx={{ color: 'text.primary' }}
                    >
                      Dashboard
                    </Button>
                    <Button
                      onClick={logout}
                      color="inherit"
                      sx={{ color: 'text.primary' }}
                    >
                      Logout
                    </Button>
                  </>
                ) : (
                  <>
                    <Button
                      component={Link}
                      href={ROUTES.LOGIN}
                      color="inherit"
                      sx={{ color: 'text.primary' }}
                    >
                      Login
                    </Button>
                    <Button
                      component={Link}
                      href={ROUTES.SIGNUP}
                      variant="contained"
                      sx={{
                        backgroundColor: 'primary.main',
                        '&:hover': {
                          backgroundColor: 'primary.dark',
                        },
                      }}
                    >
                      Get Started
                    </Button>
                  </>
                )}
              </Box>
            )}

            {/* Mobile Menu Button */}
            {isMobile && (
              <IconButton
                edge="end"
                color="inherit"
                onClick={handleMobileMenuToggle}
                sx={{ color: 'text.primary' }}
              >
                <MenuIcon />
              </IconButton>
            )}
          </Toolbar>
        </Container>
      </AppBar>

      {/* Mobile Menu */}
      {renderMobileMenu()}
    </>
  );
};

export default Header;
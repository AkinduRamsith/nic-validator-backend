'use client';

import {
  Box,
  Container,
  Grid,
  Typography,
  Link,
  Divider,
  useTheme,
  useMediaQuery,
} from '@mui/material';
import {
  Email as EmailIcon,
  Phone as PhoneIcon,
  LocationOn as LocationIcon,
} from '@mui/icons-material';
import NextLink from 'next/link';
import { ROUTES, COMPANY_INFO } from '@/utils/constants';

const Footer = () => {
  const theme = useTheme();
  const isMobile = useMediaQuery(theme.breakpoints.down('sm'));
  const currentYear = new Date().getFullYear();

  const footerSections = [
    {
      title: 'Product',
      links: [
        { label: 'Features', href: ROUTES.HOME + '#features' },
        { label: 'Pricing', href: ROUTES.PRICING },
        { label: 'API Documentation', href: '/docs' },
        { label: 'Integrations', href: '/integrations' },
      ],
    },
    {
      title: 'Company',
      links: [
        { label: 'About Us', href: ROUTES.ABOUT },
        { label: 'Contact', href: ROUTES.CONTACT },
        { label: 'Careers', href: '/careers' },
        { label: 'Blog', href: '/blog' },
      ],
    },
    {
      title: 'Resources',
      links: [
        { label: 'Help Center', href: '/help' },
        { label: 'Getting Started', href: '/getting-started' },
        { label: 'Templates', href: '/templates' },
        { label: 'Community', href: '/community' },
      ],
    },
    {
      title: 'Legal',
      links: [
        { label: 'Terms of Service', href: ROUTES.TERMS },
        { label: 'Privacy Policy', href: ROUTES.PRIVACY },
        { label: 'Cookie Policy', href: '/cookies' },
        { label: 'GDPR', href: '/gdpr' },
      ],
    },
  ];

  return (
    <Box
      component="footer"
      sx={{
        backgroundColor: 'grey.900',
        color: 'white',
        pt: 6,
        pb: 3,
        mt: 'auto',
      }}
    >
      <Container maxWidth="lg">
        {/* Main Footer Content */}
        <Grid container spacing={4}>
          {/* Company Info */}
          <Grid item xs={12} md={4}>
            <Typography
              variant="h5"
              component="div"
              sx={{
                fontWeight: 700,
                color: 'primary.light',
                mb: 2,
              }}
            >
              Mint Invoiz
            </Typography>
            <Typography
              variant="body2"
              sx={{ mb: 3, color: 'grey.300' }}
            >
              {COMPANY_INFO.tagline}
            </Typography>
            
            {/* Contact Info */}
            <Box sx={{ display: 'flex', flexDirection: 'column', gap: 1 }}>
              <Box sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
                <EmailIcon sx={{ fontSize: 16, color: 'grey.400' }} />
                <Typography variant="body2" sx={{ color: 'grey.300' }}>
                  {COMPANY_INFO.email}
                </Typography>
              </Box>
              <Box sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
                <PhoneIcon sx={{ fontSize: 16, color: 'grey.400' }} />
                <Typography variant="body2" sx={{ color: 'grey.300' }}>
                  {COMPANY_INFO.phone}
                </Typography>
              </Box>
              <Box sx={{ display: 'flex', alignItems: 'flex-start', gap: 1 }}>
                <LocationIcon sx={{ fontSize: 16, color: 'grey.400', mt: 0.2 }} />
                <Typography variant="body2" sx={{ color: 'grey.300' }}>
                  {COMPANY_INFO.address}
                </Typography>
              </Box>
            </Box>
          </Grid>

          {/* Footer Links */}
          {footerSections.map((section) => (
            <Grid item xs={6} sm={3} md={2} key={section.title}>
              <Typography
                variant="h6"
                component="div"
                sx={{
                  fontWeight: 600,
                  mb: 2,
                  fontSize: '1rem',
                }}
              >
                {section.title}
              </Typography>
              <Box sx={{ display: 'flex', flexDirection: 'column', gap: 1 }}>
                {section.links.map((link) => (
                  <Link
                    key={link.label}
                    component={NextLink}
                    href={link.href}
                    underline="none"
                    sx={{
                      color: 'grey.300',
                      fontSize: '0.875rem',
                      '&:hover': {
                        color: 'primary.light',
                        textDecoration: 'underline',
                      },
                      transition: 'color 0.3s ease',
                    }}
                  >
                    {link.label}
                  </Link>
                ))}
              </Box>
            </Grid>
          ))}
        </Grid>

        <Divider sx={{ my: 4, borderColor: 'grey.700' }} />

        {/* Bottom Footer */}
        <Box
          sx={{
            display: 'flex',
            flexDirection: isMobile ? 'column' : 'row',
            justifyContent: 'space-between',
            alignItems: 'center',
            gap: 2,
          }}
        >
          <Typography
            variant="body2"
            sx={{
              color: 'grey.400',
              textAlign: isMobile ? 'center' : 'left',
            }}
          >
            © {currentYear} {COMPANY_INFO.parent} · {COMPANY_INFO.name} · All rights reserved · 
            Developed & Designed by {COMPANY_INFO.parent} · {COMPANY_INFO.domain}
          </Typography>
          
          <Box sx={{ display: 'flex', gap: 2 }}>
            <Link
              component={NextLink}
              href={ROUTES.TERMS}
              underline="none"
              sx={{
                color: 'grey.400',
                fontSize: '0.75rem',
                '&:hover': { color: 'primary.light' },
              }}
            >
              Terms
            </Link>
            <Link
              component={NextLink}
              href={ROUTES.PRIVACY}
              underline="none"
              sx={{
                color: 'grey.400',
                fontSize: '0.75rem',
                '&:hover': { color: 'primary.light' },
              }}
            >
              Privacy
            </Link>
          </Box>
        </Box>
      </Container>
    </Box>
  );
};

export default Footer;
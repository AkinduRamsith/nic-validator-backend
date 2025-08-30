'use client';

import {
  Box,
  Container,
  Typography,
  Grid,
  Card,
  CardContent,
  Avatar,
  Stack,
} from '@mui/material';
import {
  Business as BusinessIcon,
  Speed as SpeedIcon,
  Security as SecurityIcon,
  Support as SupportIcon,
} from '@mui/icons-material';
import MainLayout from '@/components/layout/MainLayout';
import { COMPANY_INFO } from '@/utils/constants';

const AboutPage = () => {
  const values = [
    {
      icon: <BusinessIcon sx={{ fontSize: 40, color: 'primary.main' }} />,
      title: 'Business-First',
      description: 'We understand the challenges small businesses face and build solutions that truly help.',
    },
    {
      icon: <SpeedIcon sx={{ fontSize: 40, color: 'primary.main' }} />,
      title: 'Efficiency',
      description: 'Streamline your invoicing process and save time for what matters most - growing your business.',
    },
    {
      icon: <SecurityIcon sx={{ fontSize: 40, color: 'primary.main' }} />,
      title: 'Security',
      description: 'Your data is protected with enterprise-grade security and privacy measures.',
    },
    {
      icon: <SupportIcon sx={{ fontSize: 40, color: 'primary.main' }} />,
      title: 'Support',
      description: 'Our dedicated team is here to help you succeed with responsive, friendly support.',
    },
  ];

  const team = [
    {
      name: 'Alex Johnson',
      role: 'CEO & Founder',
      bio: 'Passionate about helping small businesses thrive through better technology.',
      avatar: '/team/alex.jpg',
    },
    {
      name: 'Sarah Chen',
      role: 'CTO',
      bio: 'Leading our technical vision with 10+ years in fintech and SaaS development.',
      avatar: '/team/sarah.jpg',
    },
    {
      name: 'Mike Rodriguez',
      role: 'Head of Product',
      bio: 'Focused on creating intuitive experiences that solve real business problems.',
      avatar: '/team/mike.jpg',
    },
    {
      name: 'Emily Watson',
      role: 'Head of Customer Success',
      bio: 'Ensuring every customer achieves their goals with our platform.',
      avatar: '/team/emily.jpg',
    },
  ];

  return (
    <MainLayout>
      {/* Hero Section */}
      <Box
        sx={{
          py: { xs: 8, md: 12 },
          backgroundColor: 'primary.main',
          color: 'white',
        }}
      >
        <Container maxWidth="lg">
          <Box sx={{ textAlign: 'center' }}>
            <Typography
              variant="h1"
              component="h1"
              sx={{
                fontSize: { xs: '2.5rem', md: '3.5rem' },
                fontWeight: 700,
                mb: 3,
              }}
            >
              About {COMPANY_INFO.name}
            </Typography>
            
            <Typography
              variant="h5"
              sx={{
                mb: 4,
                opacity: 0.9,
                maxWidth: 600,
                mx: 'auto',
                lineHeight: 1.6,
              }}
            >
              {COMPANY_INFO.tagline}
            </Typography>
          </Box>
        </Container>
      </Box>

      {/* Mission Section */}
      <Box sx={{ py: { xs: 8, md: 12 }, backgroundColor: 'background.default' }}>
        <Container maxWidth="lg">
          <Grid container spacing={6} alignItems="center">
            <Grid item xs={12} md={6}>
              <Typography
                variant="h2"
                component="h2"
                sx={{
                  fontWeight: 600,
                  mb: 3,
                  color: 'text.primary',
                }}
              >
                Our Mission
              </Typography>
              
              <Typography
                variant="body1"
                sx={{
                  mb: 3,
                  color: 'text.secondary',
                  lineHeight: 1.8,
                  fontSize: '1.1rem',
                }}
              >
                At {COMPANY_INFO.parent}, we believe that managing invoices shouldn't be 
                complicated or time-consuming. That's why we created {COMPANY_INFO.name} - 
                a modern, intuitive platform that makes professional invoicing accessible 
                to businesses of all sizes.
              </Typography>
              
              <Typography
                variant="body1"
                sx={{
                  color: 'text.secondary',
                  lineHeight: 1.8,
                  fontSize: '1.1rem',
                }}
              >
                We're committed to helping entrepreneurs and small business owners 
                focus on what they do best while we handle the complexities of 
                invoice management, client tracking, and payment processing.
              </Typography>
            </Grid>
            
            <Grid item xs={12} md={6}>
              <Box
                sx={{
                  height: 400,
                  backgroundColor: 'rgba(33, 150, 243, 0.1)',
                  borderRadius: 2,
                  display: 'flex',
                  alignItems: 'center',
                  justifyContent: 'center',
                  border: '2px dashed',
                  borderColor: 'primary.main',
                }}
              >
                <Typography variant="h6" color="primary.main">
                  Mission Image Placeholder
                </Typography>
              </Box>
            </Grid>
          </Grid>
        </Container>
      </Box>

      {/* Values Section */}
      <Box sx={{ py: { xs: 8, md: 12 }, backgroundColor: 'grey.50' }}>
        <Container maxWidth="lg">
          <Typography
            variant="h2"
            component="h2"
            align="center"
            sx={{
              fontWeight: 600,
              mb: 2,
              color: 'text.primary',
            }}
          >
            Our Values
          </Typography>
          
          <Typography
            variant="h6"
            align="center"
            sx={{
              mb: 6,
              color: 'text.secondary',
              maxWidth: 600,
              mx: 'auto',
            }}
          >
            The principles that guide everything we do
          </Typography>

          <Grid container spacing={4}>
            {values.map((value, index) => (
              <Grid item xs={12} sm={6} md={3} key={index}>
                <Card
                  sx={{
                    height: '100%',
                    textAlign: 'center',
                    transition: 'transform 0.3s ease',
                    '&:hover': {
                      transform: 'translateY(-4px)',
                    },
                  }}
                >
                  <CardContent sx={{ p: 4 }}>
                    <Box sx={{ mb: 2 }}>
                      {value.icon}
                    </Box>
                    <Typography
                      variant="h6"
                      component="h3"
                      sx={{
                        mb: 2,
                        fontWeight: 600,
                        color: 'text.primary',
                      }}
                    >
                      {value.title}
                    </Typography>
                    <Typography
                      variant="body2"
                      sx={{
                        color: 'text.secondary',
                        lineHeight: 1.6,
                      }}
                    >
                      {value.description}
                    </Typography>
                  </CardContent>
                </Card>
              </Grid>
            ))}
          </Grid>
        </Container>
      </Box>

      {/* Team Section */}
      <Box sx={{ py: { xs: 8, md: 12 }, backgroundColor: 'background.default' }}>
        <Container maxWidth="lg">
          <Typography
            variant="h2"
            component="h2"
            align="center"
            sx={{
              fontWeight: 600,
              mb: 2,
              color: 'text.primary',
            }}
          >
            Meet Our Team
          </Typography>
          
          <Typography
            variant="h6"
            align="center"
            sx={{
              mb: 6,
              color: 'text.secondary',
              maxWidth: 600,
              mx: 'auto',
            }}
          >
            The passionate people behind {COMPANY_INFO.name}
          </Typography>

          <Grid container spacing={4}>
            {team.map((member, index) => (
              <Grid item xs={12} sm={6} md={3} key={index}>
                <Card
                  sx={{
                    textAlign: 'center',
                    transition: 'transform 0.3s ease',
                    '&:hover': {
                      transform: 'translateY(-4px)',
                    },
                  }}
                >
                  <CardContent sx={{ p: 4 }}>
                    <Avatar
                      sx={{
                        width: 80,
                        height: 80,
                        mx: 'auto',
                        mb: 2,
                        backgroundColor: 'primary.main',
                        fontSize: '2rem',
                      }}
                    >
                      {member.name.split(' ').map(n => n[0]).join('')}
                    </Avatar>
                    
                    <Typography
                      variant="h6"
                      component="h3"
                      sx={{
                        mb: 1,
                        fontWeight: 600,
                        color: 'text.primary',
                      }}
                    >
                      {member.name}
                    </Typography>
                    
                    <Typography
                      variant="subtitle2"
                      sx={{
                        mb: 2,
                        color: 'primary.main',
                        fontWeight: 500,
                      }}
                    >
                      {member.role}
                    </Typography>
                    
                    <Typography
                      variant="body2"
                      sx={{
                        color: 'text.secondary',
                        lineHeight: 1.6,
                      }}
                    >
                      {member.bio}
                    </Typography>
                  </CardContent>
                </Card>
              </Grid>
            ))}
          </Grid>
        </Container>
      </Box>

      {/* Company Stats */}
      <Box sx={{ py: { xs: 8, md: 12 }, backgroundColor: 'primary.main', color: 'white' }}>
        <Container maxWidth="lg">
          <Grid container spacing={4} textAlign="center">
            <Grid item xs={12} sm={3}>
              <Typography variant="h3" sx={{ fontWeight: 700, mb: 1 }}>
                10K+
              </Typography>
              <Typography variant="body1" sx={{ opacity: 0.9 }}>
                Invoices Generated
              </Typography>
            </Grid>
            
            <Grid item xs={12} sm={3}>
              <Typography variant="h3" sx={{ fontWeight: 700, mb: 1 }}>
                500+
              </Typography>
              <Typography variant="body1" sx={{ opacity: 0.9 }}>
                Happy Customers
              </Typography>
            </Grid>
            
            <Grid item xs={12} sm={3}>
              <Typography variant="h3" sx={{ fontWeight: 700, mb: 1 }}>
                99.9%
              </Typography>
              <Typography variant="body1" sx={{ opacity: 0.9 }}>
                Uptime
              </Typography>
            </Grid>
            
            <Grid item xs={12} sm={3}>
              <Typography variant="h3" sx={{ fontWeight: 700, mb: 1 }}>
                24/7
              </Typography>
              <Typography variant="body1" sx={{ opacity: 0.9 }}>
                Support Available
              </Typography>
            </Grid>
          </Grid>
        </Container>
      </Box>
    </MainLayout>
  );
};

export default AboutPage;
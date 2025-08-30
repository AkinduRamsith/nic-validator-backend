'use client';

import { useState } from 'react';
import {
  Box,
  Container,
  Typography,
  Card,
  CardContent,
  Button,
  Grid,
  Switch,
  FormControlLabel,
  Stack,
  Chip,
  List,
  ListItem,
  ListItemIcon,
  ListItemText,
  Divider,
} from '@mui/material';
import {
  Check as CheckIcon,
  Close as CloseIcon,
  Star as StarIcon,
} from '@mui/icons-material';
import Link from 'next/link';
import MainLayout from '@/components/layout/MainLayout';
import { PRICING_PLANS, ROUTES } from '@/utils/constants';

const PricingPage = () => {
  const [isAnnual, setIsAnnual] = useState(false);

  const handleBillingToggle = () => {
    setIsAnnual(!isAnnual);
  };

  const formatPrice = (plan) => {
    const price = isAnnual ? plan.price.annual : plan.price.monthly;
    return price === 0 ? 'Free' : `$${price}`;
  };

  const formatPriceSubtext = (plan) => {
    if (plan.price.monthly === 0) return '';
    
    if (isAnnual) {
      return `$${Math.round(plan.price.annual / 12)}/month billed annually`;
    }
    return '/month';
  };

  const renderFeatureValue = (value) => {
    if (typeof value === 'boolean') {
      return value ? (
        <CheckIcon sx={{ color: 'success.main', fontSize: 20 }} />
      ) : (
        <CloseIcon sx={{ color: 'error.main', fontSize: 20 }} />
      );
    }
    return value;
  };

  return (
    <MainLayout>
      {/* Header Section */}
      <Box sx={{ py: { xs: 6, md: 8 }, backgroundColor: 'background.default' }}>
        <Container maxWidth="lg">
          <Box sx={{ textAlign: 'center', mb: 6 }}>
            <Typography
              variant="h1"
              component="h1"
              sx={{
                fontSize: { xs: '2.5rem', md: '3.5rem' },
                fontWeight: 700,
                mb: 3,
                color: 'text.primary',
              }}
            >
              Simple, transparent pricing
            </Typography>
            
            <Typography
              variant="h6"
              sx={{
                mb: 4,
                color: 'text.secondary',
                maxWidth: 600,
                mx: 'auto',
              }}
            >
              Choose the perfect plan for your business. Start free and upgrade as you grow.
            </Typography>

            {/* Billing Toggle */}
            <Box sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center', mb: 4 }}>
              <Typography sx={{ color: isAnnual ? 'text.secondary' : 'text.primary' }}>
                Monthly
              </Typography>
              <FormControlLabel
                control={
                  <Switch
                    checked={isAnnual}
                    onChange={handleBillingToggle}
                    color="primary"
                  />
                }
                label=""
                sx={{ mx: 1 }}
              />
              <Typography sx={{ color: isAnnual ? 'text.primary' : 'text.secondary' }}>
                Annual
              </Typography>
              {isAnnual && (
                <Chip
                  label="Save 2 months!"
                  color="primary"
                  size="small"
                  sx={{ ml: 2 }}
                />
              )}
            </Box>
          </Box>

          {/* Pricing Cards */}
          <Grid container spacing={3} justifyContent="center">
            {Object.entries(PRICING_PLANS).map(([key, plan]) => (
              <Grid item xs={12} sm={6} lg={3} key={key}>
                <Card
                  sx={{
                    height: '100%',
                    position: 'relative',
                    border: plan.popular ? 2 : 1,
                    borderColor: plan.popular ? 'primary.main' : 'divider',
                    transition: 'transform 0.3s ease, box-shadow 0.3s ease',
                    '&:hover': {
                      transform: 'translateY(-4px)',
                      boxShadow: 6,
                    },
                  }}
                >
                  {plan.popular && (
                    <Box
                      sx={{
                        position: 'absolute',
                        top: -1,
                        left: '50%',
                        transform: 'translateX(-50%)',
                        backgroundColor: 'primary.main',
                        color: 'white',
                        px: 3,
                        py: 0.5,
                        borderRadius: '0 0 20px 20px',
                        fontSize: '0.875rem',
                        fontWeight: 600,
                        display: 'flex',
                        alignItems: 'center',
                        gap: 0.5,
                      }}
                    >
                      <StarIcon sx={{ fontSize: 16 }} />
                      Most Popular
                    </Box>
                  )}
                  
                  <CardContent sx={{ p: 4, textAlign: 'center' }}>
                    <Typography
                      variant="h4"
                      component="h3"
                      sx={{
                        fontWeight: 600,
                        mb: 1,
                        color: 'text.primary',
                      }}
                    >
                      {plan.name}
                    </Typography>
                    
                    <Typography
                      variant="h2"
                      component="div"
                      sx={{
                        fontWeight: 700,
                        mb: 1,
                        color: 'primary.main',
                      }}
                    >
                      {formatPrice(plan)}
                    </Typography>
                    
                    <Typography
                      variant="body2"
                      sx={{
                        color: 'text.secondary',
                        mb: 3,
                        minHeight: 20,
                      }}
                    >
                      {formatPriceSubtext(plan)}
                    </Typography>

                    <Button
                      component={Link}
                      href={plan.name === 'Free' ? ROUTES.SIGNUP : `/checkout?plan=${key.toLowerCase()}&billing=${isAnnual ? 'annual' : 'monthly'}`}
                      variant={plan.popular ? 'contained' : 'outlined'}
                      fullWidth
                      size="large"
                      sx={{
                        mb: 4,
                        py: 1.5,
                        fontWeight: 600,
                      }}
                    >
                      {plan.name === 'Free' ? 'Get Free' : `Choose ${plan.name}`}
                    </Button>

                    <Divider sx={{ mb: 3 }} />

                    {/* Features List */}
                    <List dense>
                      <ListItem sx={{ px: 0 }}>
                        <ListItemIcon sx={{ minWidth: 36 }}>
                          <ReceiptIcon sx={{ fontSize: 20, color: 'primary.main' }} />
                        </ListItemIcon>
                        <ListItemText 
                          primary={`${plan.features.invoiceQuota} invoices/month`}
                          primaryTypographyProps={{ fontSize: '0.875rem' }}
                        />
                      </ListItem>
                      
                      <ListItem sx={{ px: 0 }}>
                        <ListItemIcon sx={{ minWidth: 36 }}>
                          {renderFeatureValue(true)}
                        </ListItemIcon>
                        <ListItemText 
                          primary={`PDF: ${Array.isArray(plan.features.pdfSizes) ? plan.features.pdfSizes.join(', ') : plan.features.pdfSizes}`}
                          primaryTypographyProps={{ fontSize: '0.875rem' }}
                        />
                      </ListItem>
                      
                      <ListItem sx={{ px: 0 }}>
                        <ListItemIcon sx={{ minWidth: 36 }}>
                          {renderFeatureValue(true)}
                        </ListItemIcon>
                        <ListItemText 
                          primary={`${plan.features.printLimit} prints/month`}
                          primaryTypographyProps={{ fontSize: '0.875rem' }}
                        />
                      </ListItem>
                      
                      <ListItem sx={{ px: 0 }}>
                        <ListItemIcon sx={{ minWidth: 36 }}>
                          {renderFeatureValue(!plan.features.watermark)}
                        </ListItemIcon>
                        <ListItemText 
                          primary={plan.features.watermark ? 'Watermark included' : 'No watermark'}
                          primaryTypographyProps={{ fontSize: '0.875rem' }}
                        />
                      </ListItem>
                      
                      <ListItem sx={{ px: 0 }}>
                        <ListItemIcon sx={{ minWidth: 36 }}>
                          {renderFeatureValue(plan.features.emailSending)}
                        </ListItemIcon>
                        <ListItemText 
                          primary="Email sending"
                          primaryTypographyProps={{ fontSize: '0.875rem' }}
                        />
                      </ListItem>
                      
                      <ListItem sx={{ px: 0 }}>
                        <ListItemIcon sx={{ minWidth: 36 }}>
                          {renderFeatureValue(plan.features.smsSending)}
                        </ListItemIcon>
                        <ListItemText 
                          primary="SMS sending"
                          primaryTypographyProps={{ fontSize: '0.875rem' }}
                        />
                      </ListItem>
                      
                      <ListItem sx={{ px: 0 }}>
                        <ListItemIcon sx={{ minWidth: 36 }}>
                          {renderFeatureValue(true)}
                        </ListItemIcon>
                        <ListItemText 
                          primary={`${plan.features.editLimit} edits/invoice`}
                          primaryTypographyProps={{ fontSize: '0.875rem' }}
                        />
                      </ListItem>
                      
                      <ListItem sx={{ px: 0 }}>
                        <ListItemIcon sx={{ minWidth: 36 }}>
                          {renderFeatureValue(true)}
                        </ListItemIcon>
                        <ListItemText 
                          primary={plan.features.support}
                          primaryTypographyProps={{ fontSize: '0.875rem' }}
                        />
                      </ListItem>
                      
                      {plan.features.paymentGateway && (
                        <ListItem sx={{ px: 0 }}>
                          <ListItemIcon sx={{ minWidth: 36 }}>
                            {renderFeatureValue(true)}
                          </ListItemIcon>
                          <ListItemText 
                            primary="Payment gateway integration"
                            primaryTypographyProps={{ fontSize: '0.875rem' }}
                          />
                        </ListItem>
                      )}
                      
                      {plan.features.analytics && (
                        <ListItem sx={{ px: 0 }}>
                          <ListItemIcon sx={{ minWidth: 36 }}>
                            {renderFeatureValue(true)}
                          </ListItemIcon>
                          <ListItemText 
                            primary="Advanced analytics"
                            primaryTypographyProps={{ fontSize: '0.875rem' }}
                          />
                        </ListItem>
                      )}
                      
                      {plan.features.whiteLabel && (
                        <ListItem sx={{ px: 0 }}>
                          <ListItemIcon sx={{ minWidth: 36 }}>
                            {renderFeatureValue(true)}
                          </ListItemIcon>
                          <ListItemText 
                            primary="White-label option"
                            primaryTypographyProps={{ fontSize: '0.875rem' }}
                          />
                        </ListItem>
                      )}
                    </List>
                  </CardContent>
                </Card>
              </Grid>
            ))}
          </Grid>
        </Container>
      </Box>

      {/* FAQ Section */}
      <Box sx={{ py: { xs: 8, md: 12 }, backgroundColor: 'grey.50' }}>
        <Container maxWidth="md">
          <Typography
            variant="h3"
            component="h2"
            align="center"
            sx={{
              mb: 6,
              fontWeight: 600,
              color: 'text.primary',
            }}
          >
            Frequently Asked Questions
          </Typography>
          
          <Stack spacing={3}>
            {[
              {
                question: 'Can I change plans anytime?',
                answer: 'Yes! You can upgrade or downgrade your plan at any time. Changes take effect immediately.',
              },
              {
                question: 'What happens when I exceed my quota?',
                answer: 'You\'ll receive notifications when approaching your limits. You can upgrade your plan or wait for the next billing cycle.',
              },
              {
                question: 'Is there a setup fee?',
                answer: 'No setup fees! Pay only for your chosen plan. Start with our free tier and upgrade when needed.',
              },
              {
                question: 'Do you offer refunds?',
                answer: 'Yes, we offer a 30-day money-back guarantee for all paid plans. No questions asked.',
              },
            ].map((faq, index) => (
              <Card key={index} sx={{ p: 2 }}>
                <CardContent>
                  <Typography variant="h6" sx={{ mb: 1, fontWeight: 600 }}>
                    {faq.question}
                  </Typography>
                  <Typography variant="body2" color="text.secondary">
                    {faq.answer}
                  </Typography>
                </CardContent>
              </Card>
            ))}
          </Stack>
        </Container>
      </Box>
    </MainLayout>
  );
};

export default PricingPage;
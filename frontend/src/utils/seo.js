import { COMPANY_INFO } from './constants';

// SEO meta tags generator
export const generateMetaTags = ({
  title,
  description,
  keywords = '',
  image = '/og-image.png',
  url = '',
  type = 'website',
}) => {
  const fullTitle = title.includes('Mint Invoiz') ? title : `${title} - Mint Invoiz`;
  const fullUrl = url ? `https://${COMPANY_INFO.domain}${url}` : `https://${COMPANY_INFO.domain}`;
  
  return {
    title: fullTitle,
    description,
    keywords,
    author: COMPANY_INFO.parent,
    viewport: 'width=device-width, initial-scale=1',
    robots: 'index, follow',
    canonical: fullUrl,
    openGraph: {
      title: fullTitle,
      description,
      url: fullUrl,
      siteName: COMPANY_INFO.name,
      type,
      locale: 'en_US',
      images: [
        {
          url: `https://${COMPANY_INFO.domain}${image}`,
          width: 1200,
          height: 630,
          alt: fullTitle,
        },
      ],
    },
    twitter: {
      card: 'summary_large_image',
      title: fullTitle,
      description,
      images: [`https://${COMPANY_INFO.domain}${image}`],
    },
  };
};

// JSON-LD structured data generators
export const generateOrganizationSchema = () => ({
  '@context': 'https://schema.org',
  '@type': 'Organization',
  name: COMPANY_INFO.name,
  alternateName: COMPANY_INFO.parent,
  url: `https://${COMPANY_INFO.domain}`,
  logo: `https://${COMPANY_INFO.domain}/logo.png`,
  contactPoint: {
    '@type': 'ContactPoint',
    telephone: COMPANY_INFO.phone,
    contactType: 'customer service',
    email: COMPANY_INFO.email,
  },
  address: {
    '@type': 'PostalAddress',
    addressLocality: 'City',
    addressRegion: 'State',
    postalCode: '12345',
    addressCountry: 'US',
  },
  sameAs: [
    'https://twitter.com/mintinvoiz',
    'https://linkedin.com/company/mintinvoiz',
  ],
});

export const generateWebsiteSchema = () => ({
  '@context': 'https://schema.org',
  '@type': 'WebSite',
  name: COMPANY_INFO.name,
  url: `https://${COMPANY_INFO.domain}`,
  description: COMPANY_INFO.tagline,
  publisher: {
    '@type': 'Organization',
    name: COMPANY_INFO.parent,
  },
  potentialAction: {
    '@type': 'SearchAction',
    target: `https://${COMPANY_INFO.domain}/search?q={search_term_string}`,
    'query-input': 'required name=search_term_string',
  },
});

export const generateProductSchema = () => ({
  '@context': 'https://schema.org',
  '@type': 'Product',
  name: COMPANY_INFO.name,
  description: 'Professional invoice management system for small businesses',
  brand: {
    '@type': 'Brand',
    name: COMPANY_INFO.parent,
  },
  offers: [
    {
      '@type': 'Offer',
      name: 'Free Plan',
      price: '0',
      priceCurrency: 'USD',
      availability: 'https://schema.org/InStock',
      url: `https://${COMPANY_INFO.domain}/pricing`,
    },
    {
      '@type': 'Offer', 
      name: 'Elite Plan',
      price: '9',
      priceCurrency: 'USD',
      availability: 'https://schema.org/InStock',
      url: `https://${COMPANY_INFO.domain}/pricing`,
    },
    {
      '@type': 'Offer',
      name: 'Pro Plan',
      price: '29',
      priceCurrency: 'USD',
      availability: 'https://schema.org/InStock',
      url: `https://${COMPANY_INFO.domain}/pricing`,
    },
    {
      '@type': 'Offer',
      name: 'Ultra Plan',
      price: '79',
      priceCurrency: 'USD',
      availability: 'https://schema.org/InStock',
      url: `https://${COMPANY_INFO.domain}/pricing`,
    },
  ],
  aggregateRating: {
    '@type': 'AggregateRating',
    ratingValue: '4.8',
    reviewCount: '127',
  },
});

// Breadcrumb schema generator
export const generateBreadcrumbSchema = (items) => ({
  '@context': 'https://schema.org',
  '@type': 'BreadcrumbList',
  itemListElement: items.map((item, index) => ({
    '@type': 'ListItem',
    position: index + 1,
    name: item.name,
    item: `https://${COMPANY_INFO.domain}${item.url}`,
  })),
});

// FAQ schema generator
export const generateFAQSchema = (faqs) => ({
  '@context': 'https://schema.org',
  '@type': 'FAQPage',
  mainEntity: faqs.map((faq) => ({
    '@type': 'Question',
    name: faq.question,
    acceptedAnswer: {
      '@type': 'Answer',
      text: faq.answer,
    },
  })),
});
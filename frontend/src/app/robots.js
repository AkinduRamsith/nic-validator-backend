import { COMPANY_INFO } from '@/utils/constants';

export default function robots() {
  const baseUrl = `https://${COMPANY_INFO.domain}`;
  
  return {
    rules: {
      userAgent: '*',
      allow: '/',
      disallow: [
        '/dashboard/',
        '/admin/',
        '/api/',
        '/_next/',
        '/private/',
      ],
    },
    sitemap: `${baseUrl}/sitemap.xml`,
  };
}
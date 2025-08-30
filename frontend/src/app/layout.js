import { Outfit } from "next/font/google";
import "./globals.css";
import Providers from '@/components/Providers';

const outfit = Outfit({
  subsets: ["latin"],
  variable: "--font-outfit",
  display: 'swap',
});

export const metadata = {
  title: "Mint Invoiz - Modern Invoice Management & Billing | OXY12",
  description: "Mint Invoiz by OXY12 — Create professional invoices, manage clients, projects, and recurring billing. Responsive, secure, and designed for small businesses. Start free today.",
  keywords: "invoice management, invoice generator, create invoices, billing software, small business invoicing, Mint Invoiz, OXY12, online invoices, PDF invoices",
  author: "OXY12",
  viewport: "width=device-width, initial-scale=1",
  robots: "index, follow",
  openGraph: {
    title: "Mint Invoiz - Modern Invoice Management & Billing",
    description: "Create professional invoices, manage clients, and track billing with Mint Invoiz by OXY12",
    url: "https://mintinvoiz.com",
    siteName: "Mint Invoiz",
    type: "website",
    locale: "en_US",
  },
  twitter: {
    card: "summary_large_image",
    title: "Mint Invoiz - Modern Invoice Management & Billing",
    description: "Create professional invoices, manage clients, and track billing with Mint Invoiz by OXY12",
  },
};

export default function RootLayout({ children }) {
  return (
    <html lang="en">
      <head>
        <link rel="preconnect" href="https://fonts.googleapis.com" />
        <link rel="preconnect" href="https://fonts.gstatic.com" crossOrigin="true" />
        <meta name="theme-color" content="#2196F3" />
        <link rel="icon" href="/favicon.ico" />
      </head>
      <body className={`${outfit.variable} antialiased`}>
        <Providers>
          {children}
        </Providers>
      </body>
    </html>
  );
}

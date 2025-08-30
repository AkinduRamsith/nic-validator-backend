# Mint Invoiz - Invoice Management System

**Fresh invoices, minted for success.**

A comprehensive invoice management system built by OXY12, featuring a Next.js frontend with Material-UI and a Spring Boot backend with JWT authentication, role-based access control, and advanced invoice management capabilities.

## 🚀 Features

### Core Features
- **Professional Invoice Generation** - Create beautiful PDF invoices with custom branding
- **Client & Project Management** - Organize clients, projects, and items efficiently  
- **Role-Based Access Control** - Admin and Customer roles with granular permissions
- **Multi-Format PDF Export** - A4, Letter, Legal formats based on subscription tier
- **Email & SMS Notifications** - Send invoices directly via email or SMS
- **Shareable Links** - Generate secure links for invoice sharing
- **Real-time Analytics** - Track business performance and invoice metrics

### Subscription Tiers

| Feature | Free | Elite | Pro | Ultra |
|---------|------|-------|-----|-------|
| **Monthly Invoices** | 25 | 100 | 500 | Unlimited |
| **PDF Formats** | A4 | A4, Letter | All | All + Templates |
| **Print Limit** | 10/month | 50/month | Unlimited | Unlimited |
| **Watermark** | Yes | No | No | No |
| **Email Sending** | ❌ | ✅ | ✅ | ✅ |
| **SMS Sending** | ❌ | ❌ | ✅ | ✅ |
| **Edit Limit** | 5/invoice | 20/invoice | 100/invoice | Unlimited |
| **Support** | Email | Email + Ticket | Live Chat | Dedicated Manager |
| **Multi-User** | ❌ | ❌ | ❌ | ✅ |
| **White Label** | ❌ | ❌ | ❌ | ✅ |

## 🏗️ Architecture

### Frontend (Next.js + MUI)
```
frontend/
├── src/
│   ├── app/                    # Next.js App Router pages
│   ├── components/
│   │   ├── common/            # Reusable components
│   │   └── layout/            # Layout components
│   ├── pages/                 # Page components
│   ├── services/              # API services
│   ├── hooks/                 # React Query hooks
│   ├── contexts/              # React contexts
│   ├── redux/                 # Redux store & reducers
│   ├── utils/                 # Utilities & constants
│   └── styles/                # MUI theme configuration
```

### Backend (Spring Boot + Java 21)
```
src/main/java/org/oxy12/mintinvoiz/
├── config/                    # Security & app configuration
├── controller/                # REST controllers
├── dto/                       # Data Transfer Objects
├── entity/                    # JPA entities
├── repository/                # JPA repositories
├── service/                   # Business logic
└── utils/                     # Utilities (JWT, Invoice numbering)
```

## 🛠️ Setup Instructions

### Prerequisites
- **Java 21** or higher
- **Node.js 18+** and npm
- **MySQL 8.0+**
- **RabbitMQ** (optional, for async logging)
- **Git**

### Backend Setup

1. **Clone and navigate to project:**
```bash
git clone <repository-url>
cd mint-invoiz
```

2. **Configure database:**
Create MySQL database:
```sql
CREATE DATABASE mint_invoiz_db;
CREATE USER 'mint_user'@'localhost' IDENTIFIED BY 'mint_password';
GRANT ALL PRIVILEGES ON mint_invoiz_db.* TO 'mint_user'@'localhost';
FLUSH PRIVILEGES;
```

3. **Set environment variables:**
Create `.env` file in root:
```env
# Database
DB_USERNAME=mint_user
DB_PASSWORD=mint_password

# JWT
JWT_SECRET=mintinvoiz-super-secret-key-change-in-production-2024

# Email (Gmail example)
MAIL_HOST=smtp.gmail.com
MAIL_PORT=587
MAIL_USERNAME=your-email@gmail.com
MAIL_PASSWORD=your-app-password

# RabbitMQ (optional)
RABBITMQ_HOST=localhost
RABBITMQ_PORT=5672
RABBITMQ_USERNAME=guest
RABBITMQ_PASSWORD=guest

# File Storage
FILE_UPLOAD_DIR=./uploads

# Payment Gateways (optional)
STRIPE_PUBLIC_KEY=pk_test_...
STRIPE_SECRET_KEY=sk_test_...
PAYPAL_CLIENT_ID=your-paypal-client-id
PAYPAL_CLIENT_SECRET=your-paypal-secret

# SMS (Twilio example)
SMS_PROVIDER=twilio
SMS_API_KEY=your-twilio-sid
SMS_API_SECRET=your-twilio-auth-token
SMS_FROM_NUMBER=+1234567890

# CORS
CORS_ALLOWED_ORIGINS=http://localhost:3000,https://mintinvoiz.com
```

4. **Run the backend:**
```bash
./mvnw spring-boot:run
```

The backend will be available at `http://localhost:8080/api`

### Frontend Setup

1. **Navigate to frontend directory:**
```bash
cd frontend
```

2. **Install dependencies:**
```bash
npm install
```

3. **Set environment variables:**
Create `.env.local` file:
```env
NEXT_PUBLIC_API_BASE_URL=http://localhost:8080/api
NEXT_PUBLIC_APP_URL=http://localhost:3000
```

4. **Run the frontend:**
```bash
npm run dev
```

The frontend will be available at `http://localhost:3000`

## 🔐 Default Admin Credentials

**Email:** admin@mintinvoiz.com  
**Password:** Admin123!

> ⚠️ **Important:** Change these credentials immediately in production!

## 📚 API Documentation

Once the backend is running, visit:
- **Swagger UI:** `http://localhost:8080/api/swagger-ui/index.html`
- **OpenAPI JSON:** `http://localhost:8080/api/v3/api-docs`

### Key API Endpoints

#### Authentication
- `POST /api/auth/login` - User login
- `POST /api/auth/signup` - User registration  
- `POST /api/auth/forgot-password` - Request password reset
- `POST /api/auth/reset-password` - Reset password with OTP
- `POST /api/auth/refresh-token` - Refresh access token

#### Invoices
- `GET /api/invoices` - Get user invoices (paginated)
- `POST /api/invoices` - Create new invoice
- `GET /api/invoices/{id}` - Get invoice details
- `GET /api/invoices/{id}/pdf` - Download invoice PDF
- `POST /api/invoices/{id}/share` - Share invoice via email/SMS/link
- `GET /api/invoices/view/{shareToken}` - View shared invoice (public)

#### Admin
- `GET /api/admin/customers` - Get all customers
- `GET /api/admin/analytics` - Get system analytics
- `POST /api/admin/customers/{id}/subscription` - Update customer subscription

## 🎨 Design System

### Color Palette
- **Primary:** #2196F3 (Charm Blue)
- **Secondary:** #757575 (Muted Gray)  
- **Background:** #FFFFFF (White)
- **Text:** #000000 (Black)

### Typography
- **Font Family:** Outfit (Google Fonts)
- **Border Radius:** 5px consistently
- **Shadows:** Subtle with smooth transitions

### Responsive Breakpoints
- **Mobile:** < 768px
- **Tablet:** 768px - 1024px  
- **Desktop:** > 1024px

## 🚀 Deployment Checklist

### Pre-Production
- [ ] Update JWT secret key
- [ ] Configure production database
- [ ] Set up SSL certificates
- [ ] Configure email/SMS providers
- [ ] Set up payment gateways
- [ ] Configure file storage (AWS S3/CloudFlare)
- [ ] Set up monitoring (Sentry/DataDog)
- [ ] Configure backup strategy

### Environment Variables (Production)
```env
# Security
JWT_SECRET=<strong-random-secret>
CORS_ALLOWED_ORIGINS=https://mintinvoiz.com

# Database
DB_USERNAME=<production-db-user>
DB_PASSWORD=<strong-db-password>

# Email
MAIL_HOST=<production-smtp-host>
MAIL_USERNAME=<production-email>
MAIL_PASSWORD=<production-email-password>

# Payment
STRIPE_SECRET_KEY=sk_live_...
PAYPAL_CLIENT_SECRET=<production-paypal-secret>

# Monitoring
SENTRY_DSN=<sentry-project-dsn>
```

### Deployment Steps
1. Build frontend: `npm run build`
2. Build backend: `./mvnw clean package -DskipTests`
3. Deploy to production servers
4. Run database migrations
5. Configure reverse proxy (Nginx)
6. Set up SSL certificates
7. Configure monitoring and logging
8. Set up automated backups

## 🧪 Testing

### Backend Tests
```bash
./mvnw test
```

### Frontend Tests  
```bash
cd frontend
npm test
```

## 📈 SEO & Marketing

### Meta Tags
Each page includes optimized meta tags for SEO:
- Title tags with keywords
- Meta descriptions
- Open Graph tags
- Twitter Card tags
- Structured data (JSON-LD)

### Marketing Pages
- **Home:** Hero section with features and CTAs
- **About:** Company information and team
- **Pricing:** Plan comparison with feature matrix
- **Contact:** Contact form and company details

## 🔧 Development

### Adding New Features
1. Create feature branch: `git checkout -b feature/new-feature`
2. Backend: Add entity → repository → service → controller → DTO
3. Frontend: Add service → hook → component → page
4. Test thoroughly
5. Submit pull request

### Code Style
- **Backend:** Follow Spring Boot conventions, use Lombok
- **Frontend:** Use functional components, custom hooks, MUI components
- **Database:** Use snake_case for columns, camelCase for entities

## 🐛 Troubleshooting

### Common Issues

**Backend won't start:**
- Check Java version: `java -version` (should be 21+)
- Verify MySQL is running: `mysql -u root -p`
- Check application.yml configuration

**Frontend build fails:**
- Clear node_modules: `rm -rf node_modules && npm install`
- Check Node.js version: `node -v` (should be 18+)
- Verify environment variables in `.env.local`

**Database connection issues:**
- Verify MySQL service is running
- Check database credentials in application.yml
- Ensure database exists and user has permissions

## 📞 Support

- **Email:** support@mintinvoiz.com
- **Documentation:** https://docs.mintinvoiz.com
- **Community:** https://community.mintinvoiz.com

---

**© 2024 OXY12 · Mint Invoiz · All rights reserved · Developed & Designed by OXY12 · mintinvoiz.com**
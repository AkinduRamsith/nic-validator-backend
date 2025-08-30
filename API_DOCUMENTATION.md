# 📚 Mint Invoiz API Documentation

## Base URL
- **Development:** `http://localhost:8080/api`
- **Production:** `https://api.mintinvoiz.com`

## Authentication
All protected endpoints require a Bearer token in the Authorization header:
```
Authorization: Bearer <access_token>
```

## Response Format
All API responses follow this standardized format:
```json
{
  "success": true,
  "message": "Operation successful",
  "data": { ... },
  "error": null,
  "statusCode": 200,
  "timestamp": "2024-01-15T10:30:00Z"
}
```

## Error Handling
Error responses include detailed information:
```json
{
  "success": false,
  "message": null,
  "data": null,
  "error": "Detailed error message",
  "statusCode": 400,
  "timestamp": "2024-01-15T10:30:00Z"
}
```

---

## 🔐 Authentication Endpoints

### POST /auth/login
Authenticate user and receive JWT tokens.

**Request Body:**
```json
{
  "email": "user@example.com",
  "password": "password123"
}
```

**Response:**
```json
{
  "success": true,
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "tokenType": "Bearer",
    "expiresIn": 900,
    "user": {
      "id": 1,
      "email": "user@example.com",
      "firstName": "John",
      "lastName": "Doe",
      "role": "CUSTOMER",
      "subscription": {
        "packageType": "FREE",
        "invoiceQuotaUsed": 5
      }
    }
  }
}
```

### POST /auth/signup
Register a new user account.

**Request Body:**
```json
{
  "email": "newuser@example.com",
  "password": "securePassword123",
  "firstName": "Jane",
  "lastName": "Smith",
  "phoneNumber": "+1234567890",
  "companyName": "Smith & Co",
  "businessRegistrationNumber": "BR123456"
}
```

### POST /auth/forgot-password
Request password reset OTP.

**Request Body:**
```json
{
  "email": "user@example.com"
}
```

### POST /auth/reset-password
Reset password using OTP.

**Request Body:**
```json
{
  "email": "user@example.com",
  "otp": "123456",
  "newPassword": "newSecurePassword123"
}
```

### POST /auth/refresh-token
Refresh access token using refresh token.

**Request Body:**
```json
{
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

---

## 🧾 Invoice Endpoints

### POST /invoices
Create a new invoice.

**Request Body:**
```json
{
  "clientId": 1,
  "projectId": 2,
  "invoiceDate": "2024-01-15",
  "dueDate": "2024-02-15",
  "items": [
    {
      "itemName": "Web Development",
      "itemDescription": "Frontend development services",
      "quantity": 40,
      "unitPrice": 75.00,
      "taxRate": 10.00,
      "discountType": "PERCENTAGE",
      "discountValue": 5.00
    }
  ],
  "notes": "Thank you for your business!",
  "termsConditions": "Payment due within 30 days",
  "paymentInstructions": "Bank transfer to account #123456789"
}
```

**Response:**
```json
{
  "success": true,
  "data": {
    "id": 1,
    "invoiceNumber": "MI-20240115-0001",
    "status": "DRAFT",
    "invoiceDate": "2024-01-15",
    "dueDate": "2024-02-15",
    "subtotal": "3000.00",
    "totalTax": "285.00",
    "totalDiscount": "150.00",
    "grandTotal": "3135.00",
    "client": {
      "id": 1,
      "companyName": "ABC Corp",
      "email": "contact@abccorp.com"
    },
    "items": [...]
  }
}
```

### GET /invoices
Get paginated list of user's invoices.

**Query Parameters:**
- `page` (int): Page number (default: 0)
- `size` (int): Page size (default: 10)
- `status` (string): Filter by status (DRAFT, SENT, PAID, OVERDUE, CANCELLED)
- `clientId` (long): Filter by client ID

**Response:**
```json
{
  "success": true,
  "data": {
    "content": [...],
    "pageable": {...},
    "totalElements": 25,
    "totalPages": 3,
    "first": true,
    "last": false
  }
}
```

### GET /invoices/{id}
Get invoice details by ID.

### GET /invoices/{id}/pdf
Download invoice as PDF file.

**Response:** Binary PDF file

### POST /invoices/{id}/share
Share invoice via email, SMS, or generate shareable link.

**Request Body:**
```json
{
  "method": "email",
  "to": "client@example.com",
  "message": "Please find your invoice attached."
}
```

**Methods:**
- `email`: Send via email
- `sms`: Send via SMS
- `link`: Generate shareable link only

### GET /invoices/view/{shareToken}
View shared invoice using share token (public endpoint).

---

## 👥 Client Endpoints

### POST /clients
Create a new client.

**Request Body:**
```json
{
  "companyName": "ABC Corporation",
  "email": "contact@abccorp.com",
  "phoneNumber": "+1234567890",
  "businessRegistrationNumber": "BR123456",
  "contactPerson": "John Smith",
  "websiteUrl": "https://abccorp.com",
  "addressLine1": "123 Business Street",
  "addressLine2": "Suite 100",
  "city": "Business City",
  "stateProvince": "BC",
  "postalCode": "12345",
  "country": "United States"
}
```

### GET /clients
Get paginated list of user's clients.

**Query Parameters:**
- `page` (int): Page number
- `size` (int): Page size  
- `search` (string): Search in company name, email, or contact person

### GET /clients/{id}
Get client details by ID.

---

## 🏗️ Project Endpoints

### POST /projects
Create a new project.

### GET /projects
Get user's projects.

### GET /projects/{id}
Get project details.

---

## 📦 Item Endpoints

### GET /items
Get global items catalog.

### POST /items
Create new global item (admin only).

---

## 👨‍💼 Admin Endpoints

### GET /admin/customers
Get all customers (admin only).

**Query Parameters:**
- `page` (int): Page number
- `size` (int): Page size
- `search` (string): Search customers

### GET /admin/analytics
Get system analytics (admin only).

**Response:**
```json
{
  "success": true,
  "data": {
    "totalCustomers": 1250,
    "invoicesThisMonth": 3450,
    "subscriptionStats": {
      "FREE": 800,
      "ELITE": 300,
      "PRO": 120,
      "ULTRA": 30
    },
    "revenue": {
      "thisMonth": 12500.00,
      "lastMonth": 11200.00,
      "growth": 11.6
    }
  }
}
```

### POST /admin/customers/{customerId}/subscription
Update customer subscription (admin only).

---

## 🔒 Security & Rate Limiting

### Rate Limits
- **Authentication endpoints:** 5 requests per minute per IP
- **Invoice creation:** 10 requests per minute per user
- **PDF generation:** 20 requests per minute per user
- **General API:** 100 requests per minute per user

### Error Codes
- **400 Bad Request:** Invalid request data
- **401 Unauthorized:** Invalid or missing token
- **403 Forbidden:** Insufficient permissions
- **404 Not Found:** Resource not found
- **429 Too Many Requests:** Rate limit exceeded
- **500 Internal Server Error:** Server error

### Quota Enforcement
API endpoints automatically enforce subscription quotas:
- Invoice creation blocked when quota exceeded
- PDF generation blocked when print quota exceeded
- Email/SMS sharing blocked based on package limitations

---

## 📱 Integration Examples

### JavaScript/React
```javascript
// Using the provided apiManager
import { apiPost, apiGet } from '@/services/apiManager';

// Create invoice
const createInvoice = async (invoiceData) => {
  try {
    const response = await apiPost('/invoices', invoiceData);
    console.log('Invoice created:', response.data);
  } catch (error) {
    console.error('Error:', error.message);
  }
};

// Get invoices
const getInvoices = async (page = 0) => {
  try {
    const response = await apiGet(`/invoices?page=${page}&size=10`);
    return response.data;
  } catch (error) {
    console.error('Error:', error.message);
  }
};
```

### cURL Examples
```bash
# Login
curl -X POST https://api.mintinvoiz.com/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"user@example.com","password":"password123"}'

# Create invoice (with auth token)
curl -X POST https://api.mintinvoiz.com/invoices \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <access_token>" \
  -d '{"clientId":1,"invoiceDate":"2024-01-15","items":[...]}'

# Download PDF
curl -X GET https://api.mintinvoiz.com/invoices/1/pdf \
  -H "Authorization: Bearer <access_token>" \
  -o invoice-1.pdf
```

---

## 🔧 Webhook Configuration

### Stripe Webhooks
Configure these webhook events in your Stripe dashboard:
- `invoice.payment_succeeded`
- `invoice.payment_failed`
- `customer.subscription.created`
- `customer.subscription.updated`
- `customer.subscription.deleted`

**Webhook Endpoint:** `https://api.mintinvoiz.com/webhooks/stripe`

### PayPal Webhooks
Configure these webhook events in PayPal:
- `BILLING.SUBSCRIPTION.CREATED`
- `BILLING.SUBSCRIPTION.CANCELLED`
- `PAYMENT.SALE.COMPLETED`

**Webhook Endpoint:** `https://api.mintinvoiz.com/webhooks/paypal`

---

## 📊 Monitoring Endpoints

### GET /actuator/health
Application health check.

### GET /actuator/info
Application information.

### GET /actuator/metrics
Application metrics (if enabled).

---

**For complete API documentation with interactive examples, visit the Swagger UI at:**
`https://api.mintinvoiz.com/swagger-ui/index.html`
-- Mint Invoiz Sample Data
-- This file contains sample data for development and testing

-- Insert default admin user
INSERT INTO users (email, password, first_name, last_name, role, is_active, email_verified, created_at, updated_at) 
VALUES ('admin@mintinvoiz.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'Admin', 'User', 'ADMIN', true, true, NOW(), NOW())
ON DUPLICATE KEY UPDATE email = email;

-- Insert sample customer user
INSERT INTO users (email, password, first_name, last_name, phone_number, company_name, business_registration_number, role, is_active, email_verified, created_at, updated_at) 
VALUES ('demo@example.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'Demo', 'User', '+1234567890', 'Demo Company LLC', 'BR123456', 'CUSTOMER', true, true, NOW(), NOW())
ON DUPLICATE KEY UPDATE email = email;

-- Insert subscription for demo user
INSERT INTO subscriptions (user_id, package_type, billing_cycle, start_date, is_active, auto_renew, invoice_quota_used, print_quota_used, edit_quota_used, email_quota_used, sms_quota_used, created_at, updated_at)
SELECT id, 'FREE', 'MONTHLY', NOW(), true, false, 0, 0, 0, 0, 0, NOW(), NOW()
FROM users WHERE email = 'demo@example.com'
ON DUPLICATE KEY UPDATE package_type = package_type;

-- Insert sample global items
INSERT INTO items (name, description, unit_price, tax_rate, unit, item_code, type, is_active, created_at, updated_at) VALUES
('Web Development', 'Frontend and backend web development services', 75.00, 0.00, 'hour', 'WEB-DEV', 'GLOBAL', true, NOW(), NOW()),
('UI/UX Design', 'User interface and user experience design', 65.00, 0.00, 'hour', 'UI-UX', 'GLOBAL', true, NOW(), NOW()),
('Consulting', 'Business and technical consulting services', 100.00, 0.00, 'hour', 'CONSULT', 'GLOBAL', true, NOW(), NOW()),
('Project Management', 'Project management and coordination', 80.00, 0.00, 'hour', 'PM', 'GLOBAL', true, NOW(), NOW()),
('Content Writing', 'Content creation and copywriting services', 45.00, 0.00, 'hour', 'CONTENT', 'GLOBAL', true, NOW(), NOW())
ON DUPLICATE KEY UPDATE name = name;

-- Insert sample client for demo user
INSERT INTO clients (user_id, company_name, email, phone_number, business_registration_number, contact_person, address_line1, city, state_province, postal_code, country, is_active, created_at, updated_at)
SELECT id, 'Sample Client Corp', 'client@samplecorp.com', '+1987654321', 'BC789012', 'Jane Smith', '456 Client Street', 'Client City', 'CC', '67890', 'United States', true, NOW(), NOW()
FROM users WHERE email = 'demo@example.com'
ON DUPLICATE KEY UPDATE company_name = company_name;

-- Insert sample project
INSERT INTO projects (client_id, name, description, start_date, end_date, status, is_active, created_at, updated_at)
SELECT c.id, 'Website Redesign Project', 'Complete website redesign and development', '2024-01-01', '2024-03-31', 'ACTIVE', true, NOW(), NOW()
FROM clients c 
JOIN users u ON c.user_id = u.id 
WHERE u.email = 'demo@example.com' AND c.email = 'client@samplecorp.com'
ON DUPLICATE KEY UPDATE name = name;
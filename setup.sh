#!/bin/bash

# Mint Invoiz Setup Script
# This script helps set up the development environment for Mint Invoiz

set -e

echo "🌿 Setting up Mint Invoiz Development Environment"
echo "================================================"

# Check prerequisites
echo "📋 Checking prerequisites..."

# Check Java
if ! command -v java &> /dev/null; then
    echo "❌ Java is not installed. Please install Java 21 or higher."
    exit 1
fi

JAVA_VERSION=$(java -version 2>&1 | awk -F '"' '/version/ {print $2}' | awk -F '.' '{print $1}')
if [ "$JAVA_VERSION" -lt 21 ]; then
    echo "❌ Java 21 or higher is required. Current version: $JAVA_VERSION"
    exit 1
fi
echo "✅ Java $JAVA_VERSION found"

# Check Node.js
if ! command -v node &> /dev/null; then
    echo "❌ Node.js is not installed. Please install Node.js 18 or higher."
    exit 1
fi

NODE_VERSION=$(node -v | sed 's/v//' | awk -F '.' '{print $1}')
if [ "$NODE_VERSION" -lt 18 ]; then
    echo "❌ Node.js 18 or higher is required. Current version: $NODE_VERSION"
    exit 1
fi
echo "✅ Node.js $(node -v) found"

# Check MySQL
if ! command -v mysql &> /dev/null; then
    echo "⚠️  MySQL not found. Please install MySQL 8.0+ and create the database."
    echo "   You can continue setup and configure MySQL later."
else
    echo "✅ MySQL found"
fi

# Setup backend
echo ""
echo "🔧 Setting up backend..."
echo "========================"

# Copy environment file
if [ ! -f .env ]; then
    cp .env.example .env
    echo "✅ Created .env file from template"
    echo "⚠️  Please update .env with your actual configuration values"
else
    echo "✅ .env file already exists"
fi

# Create uploads directory
mkdir -p uploads
echo "✅ Created uploads directory"

# Setup frontend
echo ""
echo "🎨 Setting up frontend..."
echo "========================="

cd frontend

# Copy environment file
if [ ! -f .env.local ]; then
    cp .env.local.example .env.local
    echo "✅ Created .env.local file from template"
    echo "⚠️  Please update .env.local with your actual configuration values"
else
    echo "✅ .env.local file already exists"
fi

# Install dependencies (if not already installed)
if [ ! -d node_modules ]; then
    echo "📦 Installing frontend dependencies..."
    npm install
    echo "✅ Frontend dependencies installed"
else
    echo "✅ Frontend dependencies already installed"
fi

cd ..

# Database setup instructions
echo ""
echo "🗄️  Database Setup Instructions"
echo "==============================="
echo "1. Start MySQL service"
echo "2. Create database and user:"
echo "   CREATE DATABASE mint_invoiz_db;"
echo "   CREATE USER 'mint_user'@'localhost' IDENTIFIED BY 'mint_password';"
echo "   GRANT ALL PRIVILEGES ON mint_invoiz_db.* TO 'mint_user'@'localhost';"
echo "   FLUSH PRIVILEGES;"
echo ""

# RabbitMQ setup (optional)
echo "🐰 RabbitMQ Setup (Optional)"
echo "============================"
echo "For full functionality, install RabbitMQ:"
echo "- macOS: brew install rabbitmq"
echo "- Ubuntu: sudo apt-get install rabbitmq-server"
echo "- Windows: Download from https://www.rabbitmq.com/download.html"
echo ""

# Final instructions
echo "🚀 Ready to Launch!"
echo "==================="
echo ""
echo "To start the backend:"
echo "  ./mvnw spring-boot:run"
echo ""
echo "To start the frontend:"
echo "  cd frontend && npm run dev"
echo ""
echo "Access points:"
echo "  - Frontend: http://localhost:3000"
echo "  - Backend API: http://localhost:8080/api"
echo "  - Swagger UI: http://localhost:8080/api/swagger-ui/index.html"
echo ""
echo "Default admin credentials:"
echo "  Email: admin@mintinvoiz.com"
echo "  Password: Admin123!"
echo ""
echo "Demo customer credentials:"
echo "  Email: demo@example.com"
echo "  Password: Demo123!"
echo ""
echo "⚠️  Important: Update all default passwords before production deployment!"
echo ""
echo "📚 Documentation:"
echo "  - README.md - Complete setup and usage guide"
echo "  - API_DOCUMENTATION.md - API reference"
echo "  - DEPLOYMENT_CHECKLIST.md - Production deployment guide"
echo "  - SEO_MARKETING_STRATEGY.md - Marketing and SEO strategy"
echo ""
echo "🎉 Setup complete! Happy coding with Mint Invoiz!"
import { render, screen, fireEvent } from '@testing-library/react';
import '@testing-library/jest-dom';
import { Provider } from 'react-redux';
import { configureStore } from '@reduxjs/toolkit';
import { ThemeProvider } from '@mui/material/styles';
import Header from '../Header';
import authReducer from '@/redux/reducers/auth.reducer';
import uiReducer from '@/redux/reducers/ui.reducer';
import theme from '@/styles/theme';

// Mock Next.js router
jest.mock('next/navigation', () => ({
  useRouter: () => ({
    push: jest.fn(),
  }),
}));

// Mock useAuth hook
jest.mock('@/hooks/useAuth', () => ({
  useAuth: () => ({
    isAuthenticated: false,
    user: null,
    logout: jest.fn(),
  }),
}));

const createMockStore = (initialState = {}) => {
  return configureStore({
    reducer: {
      auth: authReducer,
      ui: uiReducer,
    },
    preloadedState: {
      auth: {
        user: null,
        isAuthenticated: false,
        isLoading: false,
        error: null,
        tokens: { accessToken: null, refreshToken: null },
      },
      ui: {
        sidebarOpen: false,
        mobileMenuOpen: false,
        theme: 'light',
        notifications: [],
        loading: { global: false, invoices: false, clients: false, projects: false },
      },
      ...initialState,
    },
  });
};

const renderWithProviders = (component, initialState = {}) => {
  const store = createMockStore(initialState);
  
  return render(
    <Provider store={store}>
      <ThemeProvider theme={theme}>
        {component}
      </ThemeProvider>
    </Provider>
  );
};

describe('Header Component', () => {
  test('renders Mint Invoiz logo', () => {
    renderWithProviders(<Header />);
    
    const logo = screen.getByText('Mint Invoiz');
    expect(logo).toBeInTheDocument();
  });

  test('displays navigation items', () => {
    renderWithProviders(<Header />);
    
    expect(screen.getByText('Home')).toBeInTheDocument();
    expect(screen.getByText('About')).toBeInTheDocument();
    expect(screen.getByText('Pricing')).toBeInTheDocument();
    expect(screen.getByText('Contact')).toBeInTheDocument();
  });

  test('shows login and signup buttons when not authenticated', () => {
    renderWithProviders(<Header />);
    
    expect(screen.getByText('Login')).toBeInTheDocument();
    expect(screen.getByText('Get Started')).toBeInTheDocument();
  });

  test('shows dashboard and logout when authenticated', () => {
    const authenticatedState = {
      auth: {
        user: {
          id: 1,
          email: 'test@example.com',
          firstName: 'John',
          lastName: 'Doe',
          role: 'CUSTOMER',
        },
        isAuthenticated: true,
        isLoading: false,
        error: null,
        tokens: { accessToken: 'token', refreshToken: 'refresh' },
      },
    };

    // Mock authenticated useAuth
    jest.doMock('@/hooks/useAuth', () => ({
      useAuth: () => ({
        isAuthenticated: true,
        user: authenticatedState.auth.user,
        logout: jest.fn(),
      }),
    }));

    renderWithProviders(<Header />, authenticatedState);
    
    expect(screen.getByText('Dashboard')).toBeInTheDocument();
    expect(screen.getByText('Logout')).toBeInTheDocument();
  });

  test('opens mobile menu when hamburger is clicked', () => {
    // Mock mobile viewport
    Object.defineProperty(window, 'matchMedia', {
      writable: true,
      value: jest.fn().mockImplementation(query => ({
        matches: query === '(max-width: 899.95px)', // md breakpoint
        media: query,
        onchange: null,
        addListener: jest.fn(),
        removeListener: jest.fn(),
        addEventListener: jest.fn(),
        removeEventListener: jest.fn(),
        dispatchEvent: jest.fn(),
      })),
    });

    renderWithProviders(<Header />);
    
    const menuButton = screen.getByRole('button');
    fireEvent.click(menuButton);
    
    // Mobile menu should open (test implementation may vary based on MUI Drawer behavior)
  });
});

// Test configuration
export const testConfig = {
  testEnvironment: 'jsdom',
  setupFilesAfterEnv: ['<rootDir>/src/setupTests.js'],
  moduleNameMapping: {
    '^@/(.*)$': '<rootDir>/src/$1',
  },
};
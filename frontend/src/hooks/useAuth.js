import { useDispatch, useSelector } from 'react-redux';
import { useMutation } from '@tanstack/react-query';
import { authService } from '@/services/authService';
import {
  loginStart,
  loginSuccess,
  loginFailure,
  signupStart,
  signupSuccess,
  signupFailure,
  logout as logoutAction,
  selectAuth,
  selectUser,
  selectIsAuthenticated,
  selectAuthLoading,
  selectAuthError,
} from '@/redux/reducers/auth.reducer';
import { useRouter } from 'next/navigation';

export const useAuth = () => {
  const dispatch = useDispatch();
  const router = useRouter();
  const auth = useSelector(selectAuth);
  const user = useSelector(selectUser);
  const isAuthenticated = useSelector(selectIsAuthenticated);
  const isLoading = useSelector(selectAuthLoading);
  const error = useSelector(selectAuthError);

  // Login mutation
  const loginMutation = useMutation({
    mutationFn: authService.login,
    onMutate: () => {
      dispatch(loginStart());
    },
    onSuccess: (data) => {
      dispatch(loginSuccess(data.data));
      
      // Redirect based on role
      if (data.data.user.role === 'ADMIN') {
        router.push('/dashboard/admin');
      } else {
        router.push('/dashboard/customer');
      }
    },
    onError: (error) => {
      dispatch(loginFailure(error.message));
    },
  });

  // Signup mutation
  const signupMutation = useMutation({
    mutationFn: authService.signup,
    onMutate: () => {
      dispatch(signupStart());
    },
    onSuccess: (data) => {
      dispatch(signupSuccess(data.data));
      router.push('/dashboard/customer');
    },
    onError: (error) => {
      dispatch(signupFailure(error.message));
    },
  });

  // Forgot password mutation
  const forgotPasswordMutation = useMutation({
    mutationFn: authService.forgotPassword,
  });

  // Reset password mutation
  const resetPasswordMutation = useMutation({
    mutationFn: authService.resetPassword,
    onSuccess: () => {
      router.push('/auth/login');
    },
  });

  // Logout function
  const logout = async () => {
    try {
      await authService.logout();
    } catch (error) {
      console.error('Logout error:', error);
    } finally {
      dispatch(logoutAction());
      router.push('/');
    }
  };

  return {
    // State
    user,
    isAuthenticated,
    isLoading: isLoading || loginMutation.isPending || signupMutation.isPending,
    error: error || loginMutation.error?.message || signupMutation.error?.message,
    
    // Actions
    login: loginMutation.mutate,
    signup: signupMutation.mutate,
    forgotPassword: forgotPasswordMutation.mutate,
    resetPassword: resetPasswordMutation.mutate,
    logout,
    
    // Mutation states
    loginMutation,
    signupMutation,
    forgotPasswordMutation,
    resetPasswordMutation,
  };
};
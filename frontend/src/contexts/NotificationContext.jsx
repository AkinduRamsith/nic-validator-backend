'use client';

import { createContext, useContext } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { Snackbar, Alert, Stack } from '@mui/material';
import { 
  selectNotifications, 
  removeNotification 
} from '@/redux/reducers/ui.reducer';

const NotificationContext = createContext();

export const useNotification = () => {
  const context = useContext(NotificationContext);
  if (!context) {
    throw new Error('useNotification must be used within NotificationProvider');
  }
  return context;
};

export default function NotificationProvider({ children }) {
  const notifications = useSelector(selectNotifications);
  const dispatch = useDispatch();

  const handleClose = (notificationId) => {
    dispatch(removeNotification(notificationId));
  };

  return (
    <NotificationContext.Provider value={{}}>
      {children}
      
      {/* Notification Stack */}
      <Stack
        spacing={1}
        sx={{
          position: 'fixed',
          top: 20,
          right: 20,
          zIndex: 9999,
          maxWidth: 400,
        }}
      >
        {notifications.map((notification) => (
          <Snackbar
            key={notification.id}
            open={true}
            autoHideDuration={notification.type === 'error' ? 6000 : 4000}
            onClose={() => handleClose(notification.id)}
            anchorOrigin={{ vertical: 'top', horizontal: 'right' }}
          >
            <Alert
              onClose={() => handleClose(notification.id)}
              severity={notification.type || 'info'}
              variant="filled"
              sx={{ width: '100%' }}
            >
              {notification.message}
            </Alert>
          </Snackbar>
        ))}
      </Stack>
    </NotificationContext.Provider>
  );
}
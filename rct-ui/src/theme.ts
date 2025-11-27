import { createTheme } from '@mui/material/styles';
import type { PaletteMode } from '@mui/material';

export const getTheme = (mode: PaletteMode) =>
  createTheme({
    palette: {
      mode,
      primary: { main: '#1976d2' },
      secondary: { main: '#455a64' },
      background: {
        default: mode === 'light' ? '#f7f9fc' : '#121212',
        paper: mode === 'light' ? '#ffffff' : '#1e1e1e',
      },
      text: {
        primary: mode === 'light' ? '#000000' : '#ffffff',
        secondary: mode === 'light' ? '#607d8b' : '#b0bec5',
      },
    },
    shape: { borderRadius: 10 },
    typography: {
      fontFamily: 'Inter, Roboto, sans-serif',
    },
    components: {
      MuiPaper: {
        styleOverrides: {
          root: {
            border: mode === 'light' ? '1px solid #eef2f7' : '1px solid #333',
          },
        },
      },
      MuiDrawer: {
        styleOverrides: {
          paper: {
            borderRight: mode === 'light' ? '1px solid #e0e6ed' : '1px solid #333',
          },
        },
      },
      MuiAppBar: {
        styleOverrides: {
          root: {
            borderBottom: mode === 'light' ? '1px solid #e0e6ed' : '1px solid #333',
          },
        },
      },
    },
  });

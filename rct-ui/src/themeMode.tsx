import * as React from 'react';
import { ThemeProvider } from '@mui/material/styles';
import type { PaletteMode } from '@mui/material';
import { getTheme } from './theme';

type ThemeModeContextType = {
  mode: PaletteMode;
  toggleColorMode: () => void;
};

export const ThemeModeContext = React.createContext<ThemeModeContextType | undefined>(undefined);

function getInitialMode(): PaletteMode {
  const saved = typeof window !== 'undefined' ? window.localStorage.getItem('mui-mode') : null;
  if (saved === 'light' || saved === 'dark') return saved;
  const prefersDark =
    typeof window !== 'undefined' &&
    window.matchMedia &&
    window.matchMedia('(prefers-color-scheme: dark)').matches;
  return prefersDark ? 'dark' : 'light';
}

export const ThemeModeProvider: React.FC<React.PropsWithChildren> = ({ children }) => {
  const [mode, setMode] = React.useState<PaletteMode>(getInitialMode());

  const toggleColorMode = React.useCallback(() => {
    setMode((prev) => {
      const next = prev === 'light' ? 'dark' : 'light';
      if (typeof window !== 'undefined') window.localStorage.setItem('mui-mode', next);
      return next;
    });
  }, []);

  const ctx: ThemeModeContextType = React.useMemo(() => ({ mode, toggleColorMode }), [mode, toggleColorMode]);
  const theme = React.useMemo(() => getTheme(mode), [mode]);

  return (
    <ThemeModeContext.Provider value={ctx}>
      <ThemeProvider theme={theme}>{children}</ThemeProvider>
    </ThemeModeContext.Provider>
  );
};

export const useThemeMode = (): ThemeModeContextType => {
  const ctx = React.useContext(ThemeModeContext);
  if (!ctx) throw new Error('useThemeMode must be used within ThemeModeProvider');
  return ctx;
};
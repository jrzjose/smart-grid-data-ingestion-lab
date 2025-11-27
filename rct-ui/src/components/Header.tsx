
import * as React from 'react';
import AppBar from '@mui/material/AppBar';
import Toolbar from '@mui/material/Toolbar';
import IconButton from '@mui/material/IconButton';
import Typography from '@mui/material/Typography';
import Box from '@mui/material/Box';
import MenuIcon from '@mui/icons-material/Menu';
import Brightness6Rounded from '@mui/icons-material/Brightness6Rounded';
import { useThemeMode } from '../themeMode';

interface HeaderProps {
  drawerWidth: number;
  onMenuClick: () => void;
}

const Header: React.FC<HeaderProps> = ({ drawerWidth, onMenuClick }) => {
  const { toggleColorMode } = useThemeMode();
  return (
    <AppBar
      position="fixed"
      elevation={0}
      sx={{
        bgcolor: 'background.paper',
        color: 'text.primary',
        width: { md: `calc(100% - ${drawerWidth}px)` },
        ml: { md: `${drawerWidth}px` },
      }}
    >
      <Toolbar>
        <IconButton color="inherit" edge="start" onClick={onMenuClick} sx={{ mr: 2, display: { md: 'none' } }} aria-label="open navigation">
          <MenuIcon />
        </IconButton>

        <Typography variant="h6" noWrap sx={{ flexGrow: 1 }}>
          Company name
        </Typography>

        <Box>
          <IconButton color="inherit" aria-label="toggle theme" onClick={toggleColorMode}>
            <Brightness6Rounded />
          </IconButton>
        </Box>
      </Toolbar>
    </AppBar>
  );
};

export default Header;

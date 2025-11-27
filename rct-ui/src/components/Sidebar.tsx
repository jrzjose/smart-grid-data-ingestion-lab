
import * as React from 'react';
import Drawer from '@mui/material/Drawer';
import Box from '@mui/material/Box';
import Toolbar from '@mui/material/Toolbar';
import List from '@mui/material/List';
import ListItemButton from '@mui/material/ListItemButton';
import ListItemIcon from '@mui/material/ListItemIcon';
import ListItemText from '@mui/material/ListItemText';
import Divider from '@mui/material/Divider';
import { useLocation, useNavigate } from 'react-router-dom';
import DashboardOutlined from '@mui/icons-material/DashboardOutlined';
import ListAltOutlined from '@mui/icons-material/ListAltOutlined';
import AreaChartOutlined from '@mui/icons-material/AreaChartOutlined';
import HolidayVillageOutlined from '@mui/icons-material/HolidayVillageOutlined';

interface SidebarProps {
  drawerWidth: number;
  mobileOpen: boolean;
  onClose: () => void;
}

const menuItems: { label: string; icon: React.ReactNode; path: string }[] = [
  { label: 'Dashboard', icon: <DashboardOutlined />, path: '/' },
  { label: 'Locations', icon: <ListAltOutlined />, path: '/locations' },
  { label: 'Meters', icon: <HolidayVillageOutlined />, path: '/meters' },
  { label: 'Charts?', icon: <AreaChartOutlined />, path: '/visualizations' },
];

const Sidebar: React.FC<SidebarProps> = ({ drawerWidth, mobileOpen, onClose }) => {
  const navigate = useNavigate();
  const { pathname } = useLocation();

  const content = (
    <Box sx={{ height: '100%', display: 'flex', flexDirection: 'column' }}>
      <Toolbar />
      <Box sx={{ px: 1.5 }}>
        <List dense>
          {menuItems.map((item) => {
            const selected = pathname === item.path;
            return (
              <ListItemButton
                key={item.label}
                selected={selected}
                onClick={() => { navigate(item.path); onClose(); }}
                sx={{ mb: 0.5, borderRadius: 2 }}
              >
                <ListItemIcon sx={{ minWidth: 36 }}>{item.icon}</ListItemIcon>
                <ListItemText primary={item.label} />
              </ListItemButton>
            );
          })}
        </List>
      </Box>

      <Box sx={{ mt: 'auto', px: 1.5 }}>
        <Divider />
        <Box sx={{ fontSize: 12, color: 'text.secondary', py: 2 }}>© {new Date().getFullYear()} Company</Box>
      </Box>
    </Box>
  );

  const mobile = (
    <Drawer
      variant="temporary"
      open={mobileOpen}
      onClose={onClose}
      ModalProps={{ keepMounted: true }}
      sx={{ display: { xs: 'block', md: 'none' }, '& .MuiDrawer-paper': { boxSizing: 'border-box', width: drawerWidth } }}
    >
      {content}
    </Drawer>
  );

  const desktop = (
    <Drawer
      variant="permanent"
      open
      sx={{ display: { xs: 'none', md: 'block' }, '& .MuiDrawer-paper': { boxSizing: 'border-box', width: drawerWidth } }}
    >
      {content}
    </Drawer>
  );

  return <>{mobile}{desktop}</>;
};

export default Sidebar;

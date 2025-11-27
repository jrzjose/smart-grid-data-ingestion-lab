
import * as React from 'react';
import Box from '@mui/material/Box';
import Toolbar from '@mui/material/Toolbar';
import { Routes, Route, Navigate } from 'react-router-dom';
import Header from './components/Header';
import Sidebar from './components/Sidebar';
import Dashboard from './pages/Dashboard';
import Locations from './pages/Locations';
import Visualizations from './pages/Visualizations';
import Meters from './pages/Meters';

const drawerWidth = 260;

export default function App(): JSX.Element {
  const [mobileOpen, setMobileOpen] = React.useState(false);
  const handleToggleSidebar = () => setMobileOpen((prev) => !prev);

  return (
    <Box sx={{ display: 'flex', minHeight: '100vh', bgcolor: 'background.default' }}>
      <Header drawerWidth={drawerWidth} onMenuClick={handleToggleSidebar} />
      <Sidebar drawerWidth={drawerWidth} mobileOpen={mobileOpen} onClose={() => setMobileOpen(false)} />

      <Box component="main" sx={{ width: `calc(100% - ${drawerWidth}px)`, marginLeft: `${drawerWidth}px` }} >
        {/* sx={{ flexGrow: 1, p: { xs: 2, md: 3 }, width: { md: `calc(70% - ${drawerWidth}px)` } }} */}
        <Toolbar />
        <Routes>
          <Route path="/" element={<Dashboard />} />
          <Route path="/locations" element={<Locations />} />
          <Route path="/visualizations" element={<Visualizations />} />
          <Route path="/meters" element={<Meters />} />
          <Route path="*" element={<Navigate to="/" replace />} />
        </Routes>
      </Box>
    </Box>
  );
}


import * as React from 'react';
import Paper from '@mui/material/Paper';
import Typography from '@mui/material/Typography';

const Visualizations: React.FC = () => {
  return (
    <Paper sx={{ p: 2 }}>
      <Typography variant="h5" gutterBottom>Visualizations</Typography>
      <Typography variant="body1" color="text.secondary">
        Visualizations page. 
      </Typography>
    </Paper>
  );
};

export default Visualizations;


import * as React from 'react';
import Grid from '@mui/material/Grid';
import Paper from '@mui/material/Paper';
import Typography from '@mui/material/Typography';
import Box from '@mui/material/Box';
import { useState } from "react";

import { StompService } from "../components/StompService.ts";


const Dashboard: React.FC = () => {

  const [customerTypes, setCustomerTypes] = useState([]);

  const subCallback = (pd:any) => {
    console.log("paylodx", pd);    
    setCustomerTypes(pd.customerTypes.sort((a, b) => a.customerType.localeCompare(b.customerType)));
  }

  StompService(subCallback);

  return (
    <Grid container spacing={2}>
      <Grid size={4}></Grid>
      <Grid item xs={12} md={4}>
        <Paper sx={{ p: 2 }}>
          <Typography variant="subtitle2" gutterBottom>Consumption by Customer Types</Typography>
          <Box sx={{ display: 'flex', gap: 2 }}>
            
            <Box sx={{ bgcolor: 'primary.light', color: 'primary.contrastText', p: 2, borderRadius: 2, flex: 1 }}>
              <Typography variant="h6">{customerTypes[0] && customerTypes[0].customerType == 'c' ? customerTypes[0].energyConsumption : ''}</Typography>
              <Typography variant="body2">Residential</Typography>
            </Box>

            <Box sx={{ bgcolor: 'error.light', color: 'error.contrastText', p: 2, borderRadius: 2, flex: 1 }}>
              <Typography variant="h6">{customerTypes[1] && customerTypes[1].customerType == 'r' ? customerTypes[1].energyConsumption : ''}</Typography>
              <Typography variant="body2">Commercial</Typography>
            </Box>
          </Box>
        </Paper>
      </Grid>

      {/* <Grid item xs={12}>
        <Paper sx={{ p: 2 }}>
          <Typography variant="subtitle2" gutterBottom>Customer Engagement</Typography>
          <Box sx={{ height: 220 }}>
            <Box sx={{ display: 'flex', alignItems: 'flex-end', gap: 2, height: '100%' }}>
              {[120, 140, 110, 70, 30].map((h, i) => (
                <Box key={i} sx={{ width: 40, bgcolor: 'secondary.main', height: h, borderRadius: 1 }} />
              ))}
            </Box>
          </Box>
        </Paper>
      </Grid> */}

    </Grid>
  );
};

export default Dashboard;

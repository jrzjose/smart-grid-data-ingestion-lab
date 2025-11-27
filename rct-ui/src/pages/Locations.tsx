import * as React from 'react';
import Paper from '@mui/material/Paper';
import Typography from '@mui/material/Typography';
import { StompService } from "../components/StompService.ts";
import RctDate from "../components/RctDate.tsx";
import { useState } from "react";
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';

const Locations: React.FC = () => {
  const [locations, setLocations] = useState([]);

  const subCallback = (pd:any) => {
    console.log("paylodx", pd);    
    setLocations(pd.locations.sort((a, b) => a.locationId.localeCompare(b.locationId)));
  }

  // group meters by location
  // locations
  // customer types
  StompService(subCallback);

  return (
    <Paper sx={{ p: 2 }}>
      <Typography variant="h5" gutterBottom>Locations {locations.length}</Typography>
      <Table>
        <TableHead>
          <TableRow>
            <TableCell size="medium">Location</TableCell>
            <TableCell align="right">Energy Consumption&nbsp;(kWh)</TableCell>
            <TableCell align="right">From</TableCell>
            <TableCell align="right">To</TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {locations.map((row) => (
            <TableRow key={row.locationId} sx={{ '&:last-child td, &:last-child th': { border: 0 } }}>
              <TableCell component="th" scope="row">
                {row.locationId}
              </TableCell>
              <TableCell align="right">{row.energyConsumption.toFixed(2)}</TableCell>
              <TableCell align="right"><RctDate timestamp={row.startTime} /></TableCell>
              <TableCell align="right"><RctDate timestamp={row.endTime} /></TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </Paper>
  );
};

export default Locations;

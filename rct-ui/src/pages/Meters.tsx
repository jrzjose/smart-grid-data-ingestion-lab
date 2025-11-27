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

const Meters: React.FC = () => {
  const [meters, setMeters] = useState([]);

  const subCallback = (pd:any) => {
    console.log("paylodx", pd);    
    setMeters(pd.meters.sort((a, b) => a.meterId.localeCompare(b.meterId)));
  }

  StompService(subCallback);

  return (
    <Paper sx={{ p: 2 }}>
      <Typography variant="h5" gutterBottom>Meters {meters.length}</Typography>
      <Table>
        <TableHead>
          <TableRow>
            <TableCell size="medium">Meter Id</TableCell>
            <TableCell align="right">Energy Consumption&nbsp;(kWh)</TableCell>
            <TableCell align="right">From</TableCell>
            <TableCell align="right">To</TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {meters.map((row) => (
            <TableRow key={row.meterId} sx={{ '&:last-child td, &:last-child th': { border: 0 } }}>
              <TableCell component="th" scope="row">
                {row.meterId}
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

export default Meters;

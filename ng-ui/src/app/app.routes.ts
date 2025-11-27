import { Routes } from '@angular/router';
import { DashboardComponent } from './components/dashboard/dashboard';
import { LocationsComponent } from './components/locations/locations';
import { MetersComponent } from './components/meters/meters';
import { VisualizationsComponent } from './components/visualizations/visualizations';

export const routes: Routes = [
  { path: '', component: DashboardComponent },
  { path: 'locations', component: LocationsComponent },
  { path: 'meters', component: MetersComponent },
  { path: 'visualizations', component: VisualizationsComponent },
  { path: '**', redirectTo: '' }
];

import { Component } from '@angular/core';
import { MatCardModule } from '@angular/material/card';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-visualizations',
  imports: [CommonModule, MatCardModule],
  templateUrl: './visualizations.html',
  styleUrl: './visualizations.css',
})
export class VisualizationsComponent {}

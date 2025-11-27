import { Component, OnInit } from '@angular/core';
import { MatCardModule } from '@angular/material/card';
import { CommonModule } from '@angular/common';
import { MatTableModule } from '@angular/material/table';
import { StompService } from '../../services/StompService';
import { BehaviorSubject, Observable, of } from 'rxjs';

@Component({
  selector: 'app-dashboard',
  imports: [CommonModule, MatCardModule, MatTableModule],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css',
})
export class DashboardComponent implements OnInit{  
  public displayedColumns: string[] = ['customerType', 'energyConsumption', 'startTime', 'endTime'];
  private dataset$: Observable<any> = new Observable<any>();

  private dataSubject = new BehaviorSubject<any>([]);
  public dataSource$: Observable<any> = this.dataSubject.asObservable();
  
  constructor(private wsService: StompService) {}

  ngOnInit(): void {
    this.wsService.connect();
    this.dataset$ = this.wsService.sharedData$;
    this.locations();
  }
  
  formatDate(timestamp: number): string {
    return new Date(timestamp).toLocaleString();
  }

  public locations(): void {
    this.dataset$.subscribe({
      next: (data: any) => {
        console.log(data);
        this.dataSubject.next(data.customerTypes);
      },
      error: (err:any) => {
        console.log('Error...', err);
      }
    });
  }
}
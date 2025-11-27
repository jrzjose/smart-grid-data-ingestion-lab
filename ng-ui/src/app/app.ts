import { Component, signal, ViewChild, OnInit } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { RouterModule } from '@angular/router';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatListModule } from '@angular/material/list';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatDividerModule } from '@angular/material/divider';
import { MatSidenav } from '@angular/material/sidenav';

type Mode = 'light' | 'dark';

@Component({
  selector: 'app-root',
  imports: [
    RouterOutlet,
    RouterModule,
    MatToolbarModule,
    MatSidenavModule,
    MatListModule,
    MatIconModule,
    MatButtonModule,
    MatDividerModule
  ],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App implements OnInit {
  protected readonly title = signal('ng-ui');
  
  public currentTimeStamp: string = ""; 

  ngOnInit(): void {
    this.currentTimeStamp = new Date().toLocaleDateString(); 
  }

  @ViewChild('drawer') drawer!: MatSidenav;

  mode: Mode = this.getInitialMode();

  toggle() { }

  toggleTheme() {
    this.mode = this.mode === 'light' ? 'dark' : 'light';
    localStorage.setItem('app-theme', this.mode);
  }

  private getInitialMode(): Mode {
    const saved = localStorage.getItem('app-theme');
    if (saved === 'light' || saved === 'dark') return saved;
    const prefersDark = window.matchMedia && window.matchMedia('(prefers-color-scheme: dark)').matches;
    return prefersDark ? 'dark' : 'light';
  }
}

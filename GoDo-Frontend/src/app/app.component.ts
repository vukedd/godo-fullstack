import { Component } from '@angular/core';
import { ActivatedRoute, Router, RouterOutlet } from '@angular/router';
import { HeaderComponent } from "./layout/header/header.component";
import { ToastModule } from 'primeng/toast';
import { ToastPositionService } from './services/toast-position/toast-position.service';
import { FooterComponent } from "./layout/footer/footer.component";
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, HeaderComponent, ToastModule, FooterComponent, CommonModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'GoDo-Frontend';

  constructor(public toastPositionService: ToastPositionService, public router: Router) {}

  isLandingPage(): boolean {
    return this.router.url == '/';
  }
}

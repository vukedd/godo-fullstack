import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { HeaderComponent } from "./layout/header/header.component";
import { ToastModule } from 'primeng/toast';
import { ToastPositionService } from './services/toast-position/toast-position.service';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, HeaderComponent, ToastModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'GoDo-Frontend';

  /**
   *
   */
  constructor(public toastPositionService: ToastPositionService) {}
}

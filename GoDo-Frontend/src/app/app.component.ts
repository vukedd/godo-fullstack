import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { HeaderComponent } from "./layout/header/header.component";
import { MobileHeaderComponent } from "./layout/mobile-header/mobile-header.component";

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, HeaderComponent, MobileHeaderComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'GoDo-Frontend';
}

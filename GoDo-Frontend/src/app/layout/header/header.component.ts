import { Component } from '@angular/core';
import { ButtonModule } from 'primeng/button';
import { ThemeService } from '../../../themes/theme.service';

@Component({
  selector: 'app-header',
  imports: [ButtonModule],
  templateUrl: './header.component.html',
  styleUrl: './header.component.css',
})
export class HeaderComponent {
  constructor(public themeService: ThemeService) {}

  toggleTheme() {
    this.themeService.toggleDarkMode();
  }
}

import { Component } from '@angular/core';
import { ButtonModule } from 'primeng/button';
import { SidebarModule } from 'primeng/sidebar';
import { PanelMenuModule } from 'primeng/panelmenu';
import { ThemeService } from '../../../themes/theme.service';

@Component({
  selector: 'app-mobile-header',
  imports: [ButtonModule, SidebarModule, PanelMenuModule],
  templateUrl: './mobile-header.component.html',
  styleUrl: './mobile-header.component.css',
})
export class MobileHeaderComponent {
  visible: boolean = false;

  constructor(public themeService: ThemeService) {}

  toggleSideMenu() {
    if (this.visible) {
      this.visible = false;
    } else {
      this.visible = true;
    }
  }

  toggleTheme() {
    this.themeService.toggleDarkMode();
  }
}

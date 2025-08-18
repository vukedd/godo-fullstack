import { Component } from '@angular/core';
import { ButtonModule } from 'primeng/button';
import { ThemeService } from '../../../themes/theme.service';
import { DialogModule } from 'primeng/dialog';
import { LoginFormComponent } from "../forms/login-form/login-form.component";
import { Router } from '@angular/router';
import { SidebarModule } from 'primeng/sidebar';
import { RegisterFormComponent } from "../forms/register-form/register-form.component";

@Component({
  selector: 'app-header',
  imports: [
    ButtonModule,
    DialogModule,
    LoginFormComponent,
    SidebarModule,
    RegisterFormComponent
],
  templateUrl: './header.component.html',
  styleUrl: './header.component.css',
})
export class HeaderComponent {
  public modalVisible: boolean = false;
  public isLogin: boolean = false;
  public isRegister: boolean = false;
  public sidebarVisible: boolean = false;

  constructor(public themeService: ThemeService, public router: Router) {}

  toggleTheme() {
    this.themeService.toggleDarkMode();
  }

  toggleModal() {
    if (this.modalVisible) {
      this.modalVisible = false;
    } else {
      this.modalVisible = true;
    }
  }

   handleRegisterSuccess() {
    this.isRegister = false;
    this.isLogin = false;
  }

  handleSwitchToLogin(): void {
    this.isRegister = false;
    this.isLogin = true;
  }

  handleSwitchToRegister(): void {
    this.isLogin = false;
    this.isRegister = true;
  }

  handleLoginSuccess(): void {
    this.isLogin = false;
    this.isRegister = false;
    this.router.navigate(['/dashboard']);
  }

  openLoginModal() {
    this.isLogin = true;
  }

  openRegisterModal(){
    this.isRegister = true;
  }

  toggleSideMenu() {
    if (this.sidebarVisible) {
      this.sidebarVisible = false;
    } else {
      this.sidebarVisible = true;
    }
  }
}

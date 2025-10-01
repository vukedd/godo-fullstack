import { Component, OnInit } from '@angular/core';
import { ButtonModule } from 'primeng/button';
import { ThemeService } from '../../../themes/theme.service';
import { DialogModule } from 'primeng/dialog';
import { LoginFormComponent } from '../forms/login-form/login-form.component';
import { Router, RouterModule } from '@angular/router';
import { SidebarModule } from 'primeng/sidebar';
import { RegisterFormComponent } from '../forms/register-form/register-form.component';
import { AuthService } from '../../services/auth/auth.service';
import { CommonModule } from '@angular/common';
import { MenuModule } from 'primeng/menu';
import { MenuItem, PrimeIcons } from 'primeng/api';

@Component({
  selector: 'app-header',
  imports: [
    ButtonModule,
    DialogModule,
    LoginFormComponent,
    SidebarModule,
    RegisterFormComponent,
    CommonModule,
    RouterModule,
    MenuModule,
],
  templateUrl: './header.component.html',
  styleUrl: './header.component.css',
})
export class HeaderComponent implements OnInit {
  public modalVisible: boolean = false;
  public isLogin: boolean = false;
  public isRegister: boolean = false;
  public sidebarVisible: boolean = false;
  items: MenuItem[] | undefined;

  constructor(
    public themeService: ThemeService,
    public router: Router,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    if (this.authService.isLoggedIn()) {
      this.setupUserMenu();
    }
  }

  // navigateToDashboard() {
  //   let role = this.authService.getUserRole();

  //   if (role == "ADMIN") {
  //     this.router.navigate(['dashboard'])
  //   } else
  // }

  isPending() {
    return this.authService.isProfilePending()
  }

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
    this.setupUserMenu();
    this.router.navigate(['/dashboard']);
  }

  openLoginModal() {
    this.isLogin = true;
  }

  openRegisterModal() {
    this.isRegister = true;
  }

  toggleSideMenu() {
    if (this.sidebarVisible) {
      this.sidebarVisible = false;
    } else {
      this.sidebarVisible = true;
    }
  }

  isLoggedIn() {
    return this.authService.isLoggedIn();
  }

  isAdmin() {
    return this.authService.isAdmin();
  }

  logout() {
    this.authService.logout().subscribe();
    this.router.navigate(['']);
    this.items = undefined;
    return;
  }

  private setupUserMenu(): void {
  if (this.authService.isLoggedIn()) {
    this.items = [
      {
        label: this.authService.getUsername(),
        styleClass: 'font-bold px-3 py-2' 
      },
      {
        label: 'Profile',
        icon: 'pi pi-user',
        routerLink: '/profile', 
      },
      {
        label: 'Logout',
        icon: PrimeIcons.SIGN_OUT,
        command: () => {
          this.logout();
        },
      },
    ];
  } else {
    this.items = undefined;
  }
}
}

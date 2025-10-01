import { Component } from '@angular/core';
import { AdminHubComponent } from "../admin-hub/admin-hub.component";
import { UserHubComponent } from "../user-hub/user-hub.component";
import { AuthService } from '../../../services/auth/auth.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-dashboard',
  imports: [AdminHubComponent, UserHubComponent, CommonModule],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css'
})
export class DashboardComponent {
  constructor(public authService: AuthService) {}

  public isAdmin() : boolean {
    return this.authService.getUserRole() == "ADMIN";
  }
}

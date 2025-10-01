import { Component } from '@angular/core';
import { RouterLink, RouterModule } from '@angular/router';
import { ButtonModule } from "primeng/button";

@Component({
  selector: 'app-admin-hub',
  imports: [
    ButtonModule, 
    RouterLink,
    RouterModule
  ],
  templateUrl: './admin-hub.component.html',
  styleUrl: './admin-hub.component.css'
})
export class AdminHubComponent {}

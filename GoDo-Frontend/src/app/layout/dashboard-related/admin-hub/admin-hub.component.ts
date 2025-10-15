import { Component, OnInit } from '@angular/core';
import { RouterLink, RouterModule } from '@angular/router';
import { ButtonModule } from 'primeng/button';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-admin-hub',
  imports: [
    ButtonModule,
    RouterLink,
    RouterModule,
    CommonModule,
  ],
  templateUrl: './admin-hub.component.html',
  styleUrl: './admin-hub.component.css',
})
export class AdminHubComponent {

}

import { Component, OnInit } from '@angular/core';
import { ButtonModule } from 'primeng/button';
import { AccountRequestService } from '../../../services/accountRequest/account-request.service';
import { PendingRegistrationRequestDto } from '../../../models/accountRequest/pendingRegistrationRequestDto';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-registration-request-management',
  imports: [ButtonModule, CommonModule],
  templateUrl: './registration-request-management.component.html',
  styleUrl: './registration-request-management.component.css',
})
export class RegistrationRequestManagementComponent implements OnInit {
  pendingRegistrationRequests: PendingRegistrationRequestDto[] = [];

  constructor(public accountRequestService: AccountRequestService) {}

  formatDate(date: Date) {
    return date.toString().split('T')[0];
  }

  ngOnInit(): void {
    this.accountRequestService.getPendingAccountRequests().subscribe({
      next: (response: PendingRegistrationRequestDto[]) => {
        this.pendingRegistrationRequests = response;
      },
      error: (error) => {},
    });
  }

  approveRequest(index: number) {
    let requestId = this.pendingRegistrationRequests[index].id;

    this.accountRequestService
      .approvePendingAccountRequest(requestId)
      .subscribe({
        next: (response) => {
          if (this.pendingRegistrationRequests.length == 1) {
            this.pendingRegistrationRequests.pop();
          } else {
            this.pendingRegistrationRequests = this.pendingRegistrationRequests
              .slice(0, index)
              .concat(this.pendingRegistrationRequests.slice(index + 1));
          }
        },
      });
  }
}

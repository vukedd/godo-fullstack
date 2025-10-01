import { Component, OnInit } from '@angular/core';
import { ButtonModule } from 'primeng/button';
import { AccountRequestService } from '../../../services/accountRequest/account-request.service';
import { PendingRegistrationRequestDto } from '../../../models/accountRequest/pendingRegistrationRequestDto';
import { CommonModule } from '@angular/common';
import { RejectRequestDto } from '../../../models/accountRequest/rejectRequestDto';
import { FormControl, FormGroup, Validators, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { DialogModule } from 'primeng/dialog';
import { MessageService } from 'primeng/api';

@Component({
  selector: 'app-registration-request-management',
  imports: [ButtonModule, CommonModule, DialogModule, FormsModule, ReactiveFormsModule],
  templateUrl: './registration-request-management.component.html',
  styleUrl: './registration-request-management.component.css',
})
export class RegistrationRequestManagementComponent implements OnInit {
  pendingRegistrationRequests: PendingRegistrationRequestDto[] = [];
  rejectDialogVisibility: boolean = false;
  currentRequestIndex: number = 0;

  public rejectForm: FormGroup = new FormGroup({
    reason: new FormControl('', Validators.required),
  });

  constructor(public accountRequestService: AccountRequestService, public messageService: MessageService) {}

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

  rejectRequest() {
    let index = this.currentRequestIndex;
    let rejectRequestDto: RejectRequestDto = {
      requestId: this.pendingRegistrationRequests[index].id,
      reason: this.rejectForm.value.reason ?? ''
    }

    this.accountRequestService
      .rejectPendingAccountRequest(rejectRequestDto)
      .subscribe({
        next: (response) => {
          if (this.pendingRegistrationRequests.length == 1) {
            this.pendingRegistrationRequests.pop();
            this.hideDialog();
          } else {
            this.pendingRegistrationRequests = this.pendingRegistrationRequests
              .slice(0, index)
              .concat(this.pendingRegistrationRequests.slice(index + 1));
          }
        },
      });
  }

  showDialog(index: number) {
    this.currentRequestIndex = index;
    this.rejectDialogVisibility = true;
  }

  hideDialog() {
    this.rejectDialogVisibility = false;
  }
}

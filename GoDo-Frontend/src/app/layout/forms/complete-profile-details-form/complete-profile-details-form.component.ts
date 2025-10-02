import { Component, OnInit } from '@angular/core';
import {
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { DatePickerModule } from 'primeng/datepicker';
import { CommonModule } from '@angular/common';
import { InputMaskModule } from 'primeng/inputmask';
import { UserService } from '../../../services/user/user.service';
import { AuthService } from '../../../services/auth/auth.service';
import { UserDetailsDto } from '../../../models/user/UserDetailsDto';
import { MessageService } from 'primeng/api';
import { Router } from '@angular/router';
import { FileUploadModule } from 'primeng/fileupload';
import { TooltipModule } from 'primeng/tooltip';
import { concatMap } from 'rxjs';

@Component({
  selector: 'app-complete-profile-details-form',
  imports: [
    CommonModule,
    ReactiveFormsModule,
    InputTextModule,
    ButtonModule,
    DatePickerModule,
    InputMaskModule,
    FileUploadModule,
    TooltipModule
  ],
  templateUrl: './complete-profile-details-form.component.html',
  styleUrl: './complete-profile-details-form.component.css',
})
export class CompleteProfileDetailsFormComponent implements OnInit {
  profileDetailsForm = new FormGroup({
    username: new FormControl(
      { value: '', disabled: true },
      Validators.required
    ),
    email: new FormControl({ value: '', disabled: true }, Validators.required),
    dateOfBirth: new FormControl<null | Date>(null, Validators.required),
    city: new FormControl('', Validators.required),
    address: new FormControl('', Validators.required),
    phoneNumber: new FormControl<string | null>(null, [
      Validators.required,
      Validators.pattern(/^\d{7,8}$/),
    ]),
  });

  username: string | undefined;
  email: string | undefined;
  selectedFile: File | null = null;
  maxDate!: Date;

  constructor(
    private userService: UserService,
    public authService: AuthService,
    public messageService: MessageService,
    public router: Router
  ) {}

  ngOnInit(): void {
    if (!this.authService.isProfilePending()) {
      this.router.navigate(['dashboard']);
      return;
    }
    
    let username = this.authService.getUsername();
    if (username != undefined) {
      this.userService.getUserDeatilsFormData(username).subscribe({
        next: (response: UserDetailsDto) => {
          this.profileDetailsForm.setValue({
            username: response.username,
            email: response.email,
            dateOfBirth: null,  
            city: response.city,
            address: response.address,
            phoneNumber: response.phoneNumber,
          });

          this.username = response.username ?? '';
          this.email = response.email ?? '';
        },
        error: (error) => {
          this.messageService.add({
            summary: 'An error occured while fetching user details',
            severity: 'error',
          });
        },
      });
    }

    const today = new Date();
    this.maxDate = new Date(today.getFullYear() - 16, today.getMonth(), today.getDate());
  }

  isBtnDisabled() {
    return !this.profileDetailsForm.valid || this.selectedFile == null;
  }

  onUpload(event: any) {
    this.selectedFile = event.files[0];
  }

  submitForm() {
    let userDetails: UserDetailsDto = {
      username: this.username ?? '',
      email: this.email ?? '',
      dateOfBirth:
        this.profileDetailsForm.value.dateOfBirth
          ?.toISOString()
          .split('T')[0] ?? '',
      city: this.profileDetailsForm.value.city ?? '',
      address: this.profileDetailsForm.value.address ?? '',
      phoneNumber: `06${this.profileDetailsForm.value.phoneNumber}`,
    };

    if (this.selectedFile != null) {
      this.userService.submitUserDetailsForm(userDetails, this.selectedFile).pipe(
        concatMap(() => {
          return this.authService.refreshToken();
        })
      ).subscribe({
        next: (response) => {
          this.authService.setTokens(response)
          this.router.navigate(['dashboard']);
        },
        error: (error) => {
          this.messageService.add({
            severity: 'error',
            summary: 'An error has occurred while submiting details',
            detail: error.error.message
          })
        }
      })
    }
  }
}

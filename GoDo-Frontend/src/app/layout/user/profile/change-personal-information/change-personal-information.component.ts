import { Component, OnInit } from '@angular/core';
import {
  FormControl,
  FormGroup,
  Validators,
  FormsModule,
  ReactiveFormsModule,
} from '@angular/forms';
import { AvatarModule } from 'primeng/avatar';
import { ButtonModule } from 'primeng/button';
import { DatePickerModule } from 'primeng/datepicker';
import { DividerModule } from 'primeng/divider';
import { FileUploadModule } from 'primeng/fileupload';
import { InputTextModule } from 'primeng/inputtext';
import { UserService } from '../../../../services/user/user.service';
import { MessageService } from 'primeng/api';
import { InputMaskModule } from 'primeng/inputmask';
import { EditProfileDto } from '../../../../models/user/EditProfileDto';
import { Router } from '@angular/router';

@Component({
  selector: 'app-change-personal-information',
  imports: [
    FileUploadModule,
    AvatarModule,
    ButtonModule,
    DatePickerModule,
    InputTextModule,
    DividerModule,
    FormsModule,
    ReactiveFormsModule,
    InputMaskModule,
  ],
  templateUrl: './change-personal-information.component.html',
  styleUrl: './change-personal-information.component.css',
})
export class ChangePersonalInformationComponent implements OnInit {
  maxDate!: Date;
  selectedFile: File | null = null;
  userProfileDetails: any;
  userProfilePicture: string = '';
  temporaryFileUrl: string | null = null;
  isLoading: boolean = false;

  profileDetailsForm = new FormGroup({
    dateOfBirth: new FormControl<null | Date>(null, Validators.required),
    city: new FormControl('', Validators.required),
    address: new FormControl('', Validators.required),
    phoneNumber: new FormControl<string | null>(null, [
      Validators.required,
      Validators.pattern(/^\d{7,8}$/),
    ]),
  });

  constructor(
    public userService: UserService,
    public messageService: MessageService,
    public router: Router
  ) {}

  ngOnInit(): void {
    this.userService.getUserProfileDetails().subscribe({
      next: (response) => {
        const transformedPhoneNumber = response.phoneNumber
          ? response.phoneNumber.substring(2)
          : null;

        this.profileDetailsForm.setValue({
          dateOfBirth: new Date(response.dateOfBirth),
          city: response.city,
          address: response.address,
          phoneNumber: transformedPhoneNumber,
        });

        console.log(response);
        this.userProfilePicture = response.imagePath;
      },
      error: (error) => {
        this.messageService.add({
          severity: 'error',
          summary: 'An error has occurred while fetching user data!',
          detail: error.error.message,
        });
      },
    });

    const today = new Date();
    this.maxDate = new Date(
      today.getFullYear() - 16,
      today.getMonth(),
      today.getDate()
    );
  }

  onFileSelect(event: any) {
    const file = event.files[0];

    if (file) {
      if (this.temporaryFileUrl) {
        URL.revokeObjectURL(this.temporaryFileUrl);
      }

      this.temporaryFileUrl = URL.createObjectURL(file);
      this.selectedFile = file;

      this.userProfilePicture = this.temporaryFileUrl;
    }
  }

  submitForm() {
    this.isLoading = true;
    let userDetails: EditProfileDto = {
      dateOfBirth:
        this.profileDetailsForm.value.dateOfBirth
          ?.toISOString()
          .split('T')[0] ?? '',
      city:
        this.profileDetailsForm.value.city?.trim().split(/\s+/).join(' ') ?? '',
      address:
        this.profileDetailsForm.value.address?.trim().split(/\s+/).join(' ') ??
        '',
      phoneNumber: `06${this.profileDetailsForm.value.phoneNumber}`,
    };

    this.userService
      .changeProfileDetails(userDetails, this.selectedFile)
      .subscribe({
        next: (response) => {
          this.isLoading = false;
          this.propagateSuccess('Profile information succesfully updated!');
          this.router.navigate(['/profile']);
        },
        error: (error) => {
          this.isLoading = false;
          this.propagateSuccess(error.error.message);
        },
      });
  }

  isFormValid(): boolean {
    return (
      this.profileDetailsForm.value.address?.trim() != '' &&
      this.profileDetailsForm.value.city?.trim() != ''
    );
  }

  isBtnDisabled(): boolean {
    return this.profileDetailsForm.valid && this.isFormValid();
  }

  cancelForm() {
    this.router.navigate(['/profile']);
  }

  propagateError(message: string) {
    this.messageService.add({
      severity: 'error',
      summary: message,
    });
  }

  propagateSuccess(message: string) {
    this.messageService.add({
      severity: 'success',
      summary: 'An error has occured while updating profile information!',
      detail: message,
    });
  }
}

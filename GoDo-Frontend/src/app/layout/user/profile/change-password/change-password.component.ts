import { Component, OnInit } from '@angular/core';
import { PasswordModule } from 'primeng/password';
import { ButtonModule } from 'primeng/button';
import {
  FormControl,
  FormGroup,
  FormsModule,
  Validators,
  ReactiveFormsModule,
} from '@angular/forms';
import { TooltipModule } from 'primeng/tooltip';
import { MessageService } from 'primeng/api';
import { UserService } from '../../../../services/user/user.service';
import { AuthService } from '../../../../services/auth/auth.service';

@Component({
  selector: 'app-change-password',
  imports: [PasswordModule, ButtonModule, FormsModule, ReactiveFormsModule, TooltipModule],
  templateUrl: './change-password.component.html',
  styleUrl: './change-password.component.css',
})
export class ChangePasswordComponent implements OnInit {
  changePasswordForm!: FormGroup;
  isLoading: boolean = false;
  passwordPattern = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[^A-Za-z\d])\S{8,}$/;

  constructor(
    private messageService: MessageService,
    private userService: UserService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.changePasswordForm = new FormGroup({
      oldPassword: new FormControl('', Validators.required),
      newPassword: new FormControl('', [
        Validators.required,
        Validators.pattern(this.passwordPattern),
      ]),
      newPasswordConfirmation: new FormControl('', [
        Validators.required,
        Validators.pattern(this.passwordPattern),
      ]),
    });
  }

  isButtonDisabled(): boolean {
    return !this.changePasswordForm.valid;
  }

  submitForm() {
    this.isLoading = true;

    let oldPassword = this.changePasswordForm.value.oldPassword.trim();
    let newPassword = this.changePasswordForm.value.newPassword.trim();
    let newPasswordConfirmation = this.changePasswordForm.value.newPasswordConfirmation.trim();

    if (newPasswordConfirmation != newPassword) {
      this.propagateError("New passwords don't match!");
      this.isLoading = false;
      return;
    }

    if (oldPassword == "") {
      this.propagateError("Field can't be empty!");
      this.isLoading = false;
      return;
    }

    let username = this.authService.getUsername() ?? '';

    this.userService
    .editPassword(username, 
      {
        oldPassword: oldPassword, 
        newPassword: newPassword
      }).subscribe({
        next: (response) => {
          this.propagateSuccess("Password succesfully changed!")
          this.isLoading = false;
          this.changePasswordForm.reset()
        },
        error: (error) => {
          this.propagateError(error.error.message)
          this.isLoading = false;
        }
    })
  }

  propagateError(message: string) {
    this.messageService.add({
      severity: "error",
      summary: "An error has occurred while changing password",
      detail: message
    })
  }
  
  propagateSuccess(message: string) {
    this.messageService.add({
      severity: "success",
      summary: "Success!",
      detail: message
    })
  }
}

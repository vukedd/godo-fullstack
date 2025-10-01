import { Component, EventEmitter, Output } from '@angular/core';
import { FloatLabelModule } from 'primeng/floatlabel';
import { CommonModule } from '@angular/common';
import { InputTextModule } from 'primeng/inputtext';
import {
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { AuthService } from '../../../services/auth/auth.service';
import { Router } from '@angular/router';
import { TokenDto } from '../../../models/auth/jwt/tokenDto';
import { MessageService } from 'primeng/api';

@Component({
  selector: 'app-login-form',
  imports: [
    ReactiveFormsModule,
    CommonModule,
    FloatLabelModule,
    InputTextModule,
    ButtonModule,
  ],
  templateUrl: './login-form.component.html',
  styleUrl: './login-form.component.css',
})
export class LoginFormComponent {
  public loading: boolean = false;
  @Output() switchToRegister = new EventEmitter<void>();
  @Output() loginSuccess = new EventEmitter<void>();

  loginForm = new FormGroup({
    username: new FormControl('', Validators.required),
    password: new FormControl('', Validators.required),
  });

  constructor(
    private authService: AuthService,
    public router: Router,
    public messageService: MessageService
  ) {}

  onSignUpClick(): void {
    this.switchToRegister.emit();
  }

  isValid() {
    return this.loginForm.valid;
  }

  submitLoginForm() {
    this.loading = true;
    this.authService
      .sendLoginRequest({
        username: this.loginForm.value.username,
        password: this.loginForm.value.password,
      })
      .subscribe({
        next: (response: TokenDto) => {
          this.authService.setTokens(response);

          this.loading = false;
          this.loginForm.reset();
          this.loginSuccess.emit();

          let role = this.authService.getUserRole();
          if (role == "ADMIN") {
            this.router.navigate(['dashboard']);
            return
          } 

          this.router.navigate(['dashboard'])
        },
        error: (error) => {
          let message: string = "";
          switch (error.status) {
            case 401:
              message = "Invalid credentials";
              break;
            default:
              message = "An unknown error has occurred, if the problem persists contact support!";
          }

          this.messageService.add({
            severity: 'error',
            summary: 'Unsuccessful authentication',
            detail: message
          });
          this.loading = false;
        },
      });
  }
}

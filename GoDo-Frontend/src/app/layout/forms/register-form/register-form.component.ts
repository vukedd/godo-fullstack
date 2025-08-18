import { Component, EventEmitter, Input, Output } from '@angular/core';
import { ButtonModule } from 'primeng/button';
import { FloatLabelModule } from 'primeng/floatlabel';
import { CommonModule } from '@angular/common';
import { InputTextModule } from 'primeng/inputtext';
import {
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';

import { TooltipModule } from 'primeng/tooltip';

@Component({
  selector: 'app-register-form',
  imports: [
    FloatLabelModule, 
    ButtonModule,
    CommonModule,
    InputTextModule,
    ReactiveFormsModule,
    TooltipModule
  ],
  templateUrl: './register-form.component.html',
  styleUrl: './register-form.component.css'
})
export class RegisterFormComponent {
  @Output() switchToLogin = new EventEmitter<void>();
  @Output() registerSuccess = new EventEmitter<void>();

  public loading: boolean = false;

  registerForm = new FormGroup({
    email: new FormControl('', [Validators.required, Validators.email]),
    username: new FormControl('', [Validators.required, Validators.pattern("^[a-zA-Z0-9]{5,15}$")]),
    password: new FormControl('', [Validators.required, Validators.pattern("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$")])
  });

  isValid() {
    return this.registerForm.valid;
  }

  onSignInClick(): void {
    this.switchToLogin.emit();
  }

  onRegisterSuccess(): void {
    this.registerSuccess.emit();
  }

  submitRegisterForm() {
    this.loading = true;
    setTimeout(() => {
      this.loading = false;
    }, 3000)
  }

}

import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';
import { AuthService } from '../services/auth/auth.service';

export const RoleGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  const expectedRole = route.data['expectedRole'];
  const currentRole = authService.getUserRole();

  if (currentRole !== expectedRole) {
    router.navigate(['/dashboard']);
    return false;
  }
  return true;
};
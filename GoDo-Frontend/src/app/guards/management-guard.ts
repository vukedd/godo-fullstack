import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { Observable, of } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';
import { AuthService } from '../services/auth/auth.service';
import { ManagesService } from '../services/manages/manages.service';


@Injectable({
  providedIn: 'root'
})
export class ManagementGuard implements CanActivate {

  constructor(
    private authService: AuthService,
    private managesService: ManagesService,
    private router: Router
  ) {}

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> {

    const username = this.authService.getUsername();

    if (username === '??????' || !username) {
      this.router.navigate(['']);
      return of(false);
    }

    const venueId = route.paramMap.get('venueId');

    if (!venueId) {
      this.router.navigate(['/dashboard']);
      return of(false);
    }

   return this.managesService.doesManagementExist(venueId!, username).pipe(
      map(response => {
        const hasManagement = response && response.exists;

        if (hasManagement) {
          return true;
        } else {
          this.router.navigate(['/venues', venueId]);
          return false;
        }
      }),
      catchError((error) => {
        this.router.navigate(['']);
        return of(false); 
      })
    );
  }
}
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment.development';
import { AccountRegistrationRequest } from '../../models/auth/requests/accountRegistrationRequest';
import { LoginRequest } from '../../models/auth/requests/loginRequest';
import { TokenDto } from '../../models/auth/tokenDto';
import { jwtDecode } from 'jwt-decode';
import { MyJwtPayload } from '../../models/auth/myJwtPayload';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  constructor(private http: HttpClient) {}

  public sendRegistrationRequest(
    request: AccountRegistrationRequest
  ): Observable<any> {
    return this.http.post(environment.apiUrl + '/auth/register', request);
  }

  public sendLoginRequest(request: LoginRequest): Observable<any> {
    return this.http.post(environment.apiUrl + '/auth/login', request);
  }

  public handleLogin(token: TokenDto) {
    if (typeof window !== 'undefined' && window.localStorage) {
      localStorage.setItem('accessToken', token.accessToken);
    }
  }

  setAccessToken(token: string) {
    if (typeof window !== 'undefined' && window.localStorage) {
      localStorage.setItem('accessToken', token);
    }
  }

  getAccessToken(): string | null {
    if (typeof window !== 'undefined' && window.localStorage) {
      return localStorage.getItem('accessToken');
    }
    return null;
  }

  isLoggedIn() {
    if (typeof window !== 'undefined' && window.localStorage) {
      if (
        localStorage.getItem('accessToken') != null //&&
        // localStorage.getItem('refreshToken') != null
      ) {
        return true;
      }
    }

    return false;
  }

  logout() {
    if (typeof window !== 'undefined' && window.localStorage) {
      localStorage.clear();
    }
  }

  getUsername() {
    let token: string | null = this.getAccessToken();
    
    if (token == null)
      return "??????";

    let decodedToken = jwtDecode(token);

    return decodedToken.sub;
  }

  isAdmin(): boolean{
    let token: string | null = this.getAccessToken();
    
    if (token == null)
      return false;

    let decodedToken = jwtDecode<MyJwtPayload>(token);
    return decodedToken.role == "ADMIN";
  }
}

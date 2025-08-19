import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment.development';
import { AccountRegistrationRequest } from '../../models/auth/accountRegistrationRequest';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  constructor(private http: HttpClient) { }

  public sendRegistrationRequest(request: AccountRegistrationRequest): Observable<any> {
    return this.http.post(environment.apiUrl + "/accountRequest", request);
  }
}

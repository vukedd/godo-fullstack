import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment.development';
import { enableDebugTools } from '@angular/platform-browser';

@Injectable({
  providedIn: 'root'
})
export class AccountRequestService {
  constructor(private http: HttpClient) {}

  public getPendingAccountRequests(): Observable<any> {
    return this.http.get(environment.apiUrl + "/account-request/pending");
  }

  public approvePendingAccountRequest(requestId: number): Observable<any> {
    return this.http.post(environment.apiUrl + "/account-request/approve", {requestId: requestId});
  }
}

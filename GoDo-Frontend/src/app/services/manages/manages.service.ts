import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environment.development';

@Injectable({
  providedIn: 'root'
})
export class ManagesService {
  constructor(public http: HttpClient) {}

  public getManagementByUsername(username: string) {
    return this.http.get(`${environment.apiUrl}/manages/manager/${username}`)
  }
}

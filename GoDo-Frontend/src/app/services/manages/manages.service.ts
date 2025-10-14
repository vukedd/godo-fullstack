import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environment.development';
import { ManagerOptionDto } from '../../models/user/ManagerOptionDto';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ManagesService {
  constructor(public http: HttpClient) {}

  public getManagementByUsername(username: string) {
    return this.http.get(`${environment.apiUrl}/manages/manager/${username}`)
  }

  public getManagersByVenueId(venueId: number) {
    return this.http.get(`${environment.apiUrl}/manages/venue/${venueId}/options`)
  }

  public updateManagers(venueId: number, selectedUsers: ManagerOptionDto[]) : Observable<any>{
    return this.http.put(`${environment.apiUrl}/manages/venue/update/${venueId}`, selectedUsers);
  }

  public doesManagementExist(venueId: string, username: string | undefined) : Observable<any>{
    return this.http.get(`${environment.apiUrl}/manages/venue/exists/${venueId}/${username}`);
  }
}

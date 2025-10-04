import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environment.development';
import { Observable } from 'rxjs';
import { UserDetailsDto } from '../../models/user/UserDetailsDto';
import { EditPasswordDto } from '../../models/user/EditPasswordDto';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  constructor(private http: HttpClient) {}

  public getUserDetailsFormData(username: String): Observable<any> {
    return this.http.get(`${environment.apiUrl}/user/details/${username}`)
  }

  public submitUserDetailsForm(data: UserDetailsDto, profilePicture: File): Observable<any> {
    let formData = new FormData();
    formData.append('userDetails', JSON.stringify(data));
    formData.append('image', profilePicture);
    return this.http.patch(`${environment.apiUrl}/user/update`, formData);
  }

  public getUserProfileDetails(username: String): Observable<any> {
    return this.http.get(`${environment.apiUrl}/user/profile/${username}`);
  }

  public editPassword(username: String, request: EditPasswordDto) {
    return this.http.patch(`${environment.apiUrl}/user/edit-password/${username}`, request);
  }
}

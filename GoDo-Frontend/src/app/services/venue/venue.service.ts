import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environment.development';
import { AddVenueRequest } from '../../models/venue/AddVenueRequest';

@Injectable({
  providedIn: 'root'
})
export class VenueService {
  constructor(private http: HttpClient) {}

  public addVenue(venueData: AddVenueRequest, venueImage: File) {
    let formData = new FormData();
    formData.append("venue", JSON.stringify(venueData));
    formData.append("image", venueImage);

    return this.http.post(environment.apiUrl + "/venue", formData);
  }
}

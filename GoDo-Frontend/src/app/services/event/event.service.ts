import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { CreateEventDto } from '../../models/event/createEventDto';
import { environment } from '../../../environments/environment.development';

@Injectable({
  providedIn: 'root'
})
export class EventService {
  constructor(private http: HttpClient) {}

  public createEvent(dto: CreateEventDto, image: File, venueId: string) {
    let formData = new FormData();
    formData.append('event', JSON.stringify(dto));
    formData.append('image', image);

    return this.http.post(`${environment.apiUrl}/event/${venueId}`, formData);
  }
}

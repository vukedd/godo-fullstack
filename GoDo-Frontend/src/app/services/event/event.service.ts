import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { CreateEventDto } from '../../models/event/createEventDto';
import { environment } from '../../../environments/environment.development';
import { Observable } from 'rxjs';
import { FilterEventDto } from '../../models/event/FilterEventDto';

@Injectable({
  providedIn: 'root',
})
export class EventService {
  constructor(private http: HttpClient) {}

  public filterEvents(
    dto: FilterEventDto,
    pageNumber: number
  ): Observable<any> {
    let eventTypeParam = null;

    if (dto.eventType != null) {
      eventTypeParam = dto.eventType;
    }

    let queryParameters = '?page=' + pageNumber.toString() + '&size=8';

    if (dto.filter != "") {
      queryParameters += '&filter=' + dto.filter;
    }

    if (dto.eventType != -1) {
      queryParameters += '&eventType=' + dto.eventType;
    }

    if (dto.priceFrom != -1) {
      queryParameters += '&priceFrom=' + dto.priceFrom?.toString();
    }

    if (dto.priceTo != -1) {
      queryParameters += '&priceTo=' + dto.priceTo?.toString();
    }

    if (dto.date != "") {
      queryParameters += '&date=' + dto.date?.toString();
    }

    return this.http.get(environment.apiUrl + '/event' + queryParameters);
  }

  public createEvent(dto: CreateEventDto, image: File, venueId: string) {
    let formData = new FormData();
    formData.append('event', JSON.stringify(dto));
    formData.append('image', image);

    return this.http.post(`${environment.apiUrl}/event/${venueId}`, formData);
  }

  public getUpcomingEventsByVenueId(venueId: string): Observable<any> {
    return this.http.get(`${environment.apiUrl}/event/upcoming/${venueId}`);
  }

  public getEventById(eventId: string): Observable<any> {
    return this.http.get(`${environment.apiUrl}/event/${eventId}`);
  }
}

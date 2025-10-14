import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { CreateReviewDto } from '../../models/review/CreateReviewDto';
import { environment } from '../../../environments/environment.development';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ReviewService {
  constructor(public http: HttpClient) {}

  public getReviewsByVenueId(id: string) : Observable<any> {
    return this.http.get(`${environment.apiUrl}/review/${id}`);
  }

  public createReview(dto: CreateReviewDto) {
    return this.http.post(`${environment.apiUrl}/review`, dto);
  }

  public getRatingOverview(venueId: string) : Observable<any> {
    return this.http.get(`${environment.apiUrl}/review/overview/${venueId}`);
  }

  public getRatingPageByVenueId(venueId: string, page: number) : Observable<any> {
    return this.http.get(`${environment.apiUrl}/review/venue/${venueId}?page=${page}`)
  }

  public hideReview(reviewId: number): Observable<any> {
    return this.http.patch(`${environment.apiUrl}/review/hide/${reviewId}`, {});
  }

  public deleteReview(reviewId: number): Observable<any> {
    return this.http.patch(`${environment.apiUrl}/review/delete/${reviewId}`, {});
  }

  public getProfileReviewList() :Observable<any> {
    return this.http.get(`${environment.apiUrl}/review/user`);
  }
}

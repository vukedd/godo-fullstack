import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { CreateReviewDto } from '../../models/review/CreateReviewDto';
import { environment } from '../../../environments/environment.development';

@Injectable({
  providedIn: 'root'
})
export class ReviewService {
  constructor(public http: HttpClient) {}

  public createReview(dto: CreateReviewDto) {
    return this.http.post(`${environment.apiUrl}/review`, dto);
  }
}

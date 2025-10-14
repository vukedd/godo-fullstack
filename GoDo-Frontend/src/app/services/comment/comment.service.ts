import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environment.development';

@Injectable({
  providedIn: 'root'
})
export class CommentService {
  constructor(private http: HttpClient) {}

  public replyToReview(reviewId: number, comment: string) {
    return this.http.post(`${environment.apiUrl}/comment/reply`, {reviewId: reviewId, commentContent: comment})
  }
}

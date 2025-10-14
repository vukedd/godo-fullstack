import { CommonModule } from '@angular/common';
import { Component, Input, OnInit } from '@angular/core';
import {
  FormControl,
  FormGroup,
  FormsModule,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { AvatarModule } from 'primeng/avatar';
import { ButtonModule } from 'primeng/button';
import { CardModule } from 'primeng/card';
import { DividerModule } from 'primeng/divider';
import { RatingModule } from 'primeng/rating';
import { ReviewOverviewDto } from '../../../models/review/ReviewOverviewDto';
import { RouterModule } from '@angular/router';
import { AuthService } from '../../../services/auth/auth.service';
import { DialogModule } from 'primeng/dialog';
import { CommentService } from '../../../services/comment/comment.service';
import { ReviewService } from '../../../services/review/review.service';

@Component({
  selector: 'app-review-card',
  imports: [
    FormsModule,
    CardModule,
    AvatarModule,
    ButtonModule,
    RatingModule,
    CommonModule,
    DividerModule,
    RouterModule,
    DialogModule,
    ReactiveFormsModule,
    FormsModule,
  ],
  templateUrl: './review-card.component.html',
  styleUrl: './review-card.component.css',
})
export class ReviewCardComponent implements OnInit {
  @Input() review!: ReviewOverviewDto;
  @Input() isManager: boolean = false;
  @Input() isReviewPage: boolean = false;

  isReplyDialogShown: boolean = false;
  replyForm: FormGroup = new FormGroup({
    content: new FormControl('', Validators.required),
  });

  constructor(
    public authService: AuthService,
    public commentService: CommentService,
    public reviewService: ReviewService
  ) {}

  ngOnInit(): void {}

  showReplyDialog() {
    this.isReplyDialogShown = true;
  }

  isSubmitBtnDisabled() {
    return !this.replyForm.valid;
  }

  submitReply() {
    let reviewId = this.review.reviewId;
    let content = this.replyForm.value.content;

    this.commentService.replyToReview(reviewId, content).subscribe({
      next: (response) => {
        this.isReplyDialogShown = false;
        this.replyForm.reset();
        location.reload();
      },
      error: (error) => {},
    });
  }

  deleteReview(reviewId: number) {
    this.reviewService.deleteReview(reviewId)
    .subscribe({
      next: (response) => {
        location.reload()
      },
      error: (error) => {

      }
    })
  }

  hideReview(reviewId: number) {
    this.reviewService.hideReview(reviewId)
    .subscribe({
      next: (response) => {
        location.reload()
      },
      error: (error) => {
        
      }
    })
  }
}

import { CommentDto } from "../comment/CommentDto";

export interface ReviewOverviewDto {
  reviewId: number;
  reviewer: string;
  reviewerPfpPath: string;
  eventId: number;
  eventName: string;
  reviewStatus: string;
  eventCount: number;
  comment: string;
  performanceGrade: number;
  venueGrade: number;
  ambientGrade: number;
  overallImpression: number;
  reviewVenueReply?: CommentDto | null; 
}
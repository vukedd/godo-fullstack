export interface FilterVenueDto {
    filter: string | null,
    minReviewCount: number | null;
    maxReviewCount: number | null;

    // Ratings Ranges (mapped to VenueDocument fields)
    minRatingPerformance: number | null;
    maxRatingPerformance: number | null;

    minRatingAmbient: number | null;
    maxRatingAmbient: number | null;

    minRatingVenue: number | null;
    maxRatingVenue: number | null;

    minRatingOverall: number | null;
    maxRatingOverall: number | null;

    // Operator to combine these range fields (Default to AND)
    operator: LogicalOperator | null;
}

export enum LogicalOperator {
  AND = 'AND',
  OR = 'OR'
}
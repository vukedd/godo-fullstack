export interface CreateReviewDto {
    reviewer: string,
    venueId: number,
    eventId: number,
    comment: string,
    performanceGrade: number,
    venueGrade: number,
    ambientGrade: number,
    overallImpression: number
}
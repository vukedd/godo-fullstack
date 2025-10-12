export interface EventOverviewDto {
  id: number;
  name: string;
  description: string;
  date: string;
  type: string;
  price: number;
  imagePath: string;
  recurrent: boolean;
  venueId: number;
}

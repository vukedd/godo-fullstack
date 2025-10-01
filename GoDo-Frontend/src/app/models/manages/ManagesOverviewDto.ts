import { VenueOverviewDto } from "../venue/VenueOverviewDto";

export interface ManagesOverviewDto {
    id: number,
    venue: VenueOverviewDto,
    manager: string,
}
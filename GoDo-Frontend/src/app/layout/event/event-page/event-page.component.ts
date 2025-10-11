import { Component, OnInit } from '@angular/core';
import { EventService } from '../../../services/event/event.service';
import { ActivatedRoute } from '@angular/router';
import { TooltipModule } from 'primeng/tooltip';
import { CommonModule } from '@angular/common';
import { EventPageVenueCardComponent } from '../../venue/event-page-venue-card/event-page-venue-card.component';
import { VenueService } from '../../../services/venue/venue.service';
import { switchMap } from 'rxjs';

@Component({
  selector: 'app-event-page',
  imports: [TooltipModule, CommonModule, EventPageVenueCardComponent],
  templateUrl: './event-page.component.html',
  styleUrl: './event-page.component.css',
})
export class EventPageComponent implements OnInit {
  public event: any;
  public eventType: any;
  public venue: any;
  public isRecurrent: boolean = false;

  public eventTypeMap = new Map<string, string>([
    ['PARTY', 'Party'],
    ['FESTIVAL', 'Festival'],
    ['STANDUP_AND_THEATER', 'Standup & Theater'],
    ['MISCELLANEOUS', 'Miscellaneous'],
    ['CULTURE', 'Culture'],
    ['EXHIBITION', 'Exhibition'],
    ['CONCERT', 'Concert'],
    ['OPEN_MIC', 'Open Mic'],
    ['QUIZ', 'Quiz'],
    ['MOVIE', 'Movie'],
  ]);

  private readonly eventTypeColorMap = new Map<string, string>([
    ['PARTY', '#E91E63'],
    ['FESTIVAL', '#FF9800'],
    ['STANDUP_AND_THEATER', '#9C27B0'],
    ['MISCELLANEOUS', '#607D8B'],
    ['CULTURE', '#2196F3'],
    ['EXHIBITION', '#4CAF50'],
    ['CONCERT', '#FF5722'],
    ['OPEN_MIC', '#00BCD4'],
    ['QUIZ', '#8BC34A'],
    ['MOVIE', '#3F51B5'],
  ]);

  constructor(
    private eventService: EventService,
    private route: ActivatedRoute,
    private venueService: VenueService
  ) {}

  eventTypeColor(event: any): string {
    if (!event || !event.type) {
      return '#BDBDBD';
    }

    const eventTypeKey = event.type.toUpperCase();
    return this.eventTypeColorMap.get(eventTypeKey) || '#BDBDBD';
  }

  ngOnInit(): void {
    let eventId = this.route.snapshot.paramMap.get('id') ?? '0';

    this.eventService
      .getEventById(eventId)
      .pipe(
        switchMap((event) => {
          this.event = event;
          this.eventType = event.type;
          this.isRecurrent = event.recurrent;
          return this.venueService.findVenueById(event.venueId);
        })
      )
      .subscribe({
        next: (venue) => {
          this.venue = venue;
          console.log(venue);
        },
        error: (err) => {
          console.log(err);
        },
      });
  }
}

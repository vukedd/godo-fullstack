import { Component, Input } from '@angular/core';
import { RouterModule } from '@angular/router';
import { ButtonModule } from 'primeng/button';
import { CardModule } from 'primeng/card';

@Component({
  selector: 'app-upcoming-event-card',
  imports: [ButtonModule, RouterModule, CardModule],
  templateUrl: './upcoming-event-card.component.html',
  styleUrl: './upcoming-event-card.component.css',
})
export class UpcomingEventCardComponent {
  @Input() event: any;

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

  eventTypeColor(event: any): string {
    if (!event || !event.eventType) {
      return '#BDBDBD';
    }

    const eventTypeKey = event.eventType.toUpperCase();

    return this.eventTypeColorMap.get(eventTypeKey) || '#BDBDBD';
  }
}

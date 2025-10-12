import { Component, OnInit } from '@angular/core';
import { ManagesService } from '../../../services/manages/manages.service';
import { AuthService } from '../../../services/auth/auth.service';
import { VenueService } from '../../../services/venue/venue.service';
import { ManagesOverviewDto } from '../../../models/manages/ManagesOverviewDto';
import { CommonModule } from '@angular/common';
import { EventCardComponent } from "../../event/event-card/event-card.component";
import { EventOverviewDto } from '../../../models/event/EventOverviewDto';
import { EventService } from '../../../services/event/event.service';

@Component({
  selector: 'app-user-hub',
  imports: [CommonModule, EventCardComponent],
  templateUrl: './user-hub.component.html',
  styleUrl: './user-hub.component.css',
})
export class UserHubComponent implements OnInit {
  public events: EventOverviewDto[] = [];

  constructor(
    public managesService: ManagesService,
    public authService: AuthService,
    public venueService: VenueService,
    public eventService: EventService
  ) {}

  public managementOverviewList: ManagesOverviewDto[] = [];

  ngOnInit(): void {
    let username = this.authService.getUsername() ?? '';

    // this.managesService
    //   .getManagementByUsername(username)
    //   .pipe(
    //     switchMap((dtos: any) => {
    //       const observables = dtos.map((dto: any) =>
    //         forkJoin({
    //           venue: this.venueService.findVenueById(dto.venueId),
    //           manager: of(dto.managerUsername),
    //           management: of(dto),
    //         }).pipe(
    //           map(
    //             ({ venue, manager, management }) =>
    //               ({
    //                 id: management.id,
    //                 venue: venue,
    //                 manager: manager,
    //               } as ManagesOverviewDto)
    //           )
    //         )
    //       );

    //       return forkJoin(observables);
    //     })
    //   )
    //   .subscribe((managementOverviewList: any) => {
    //     this.managementOverviewList = managementOverviewList;
    //   });

    this.eventService.getEventsHappeningToday()
    .subscribe({
      next: (response) => {
        this.events = response;
      }, error: (error) => {
        
      }
    })

  }
}

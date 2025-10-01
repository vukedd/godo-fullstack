import { Component, OnInit } from '@angular/core';
import { ManagesService } from '../../../services/manages/manages.service';
import { AuthService } from '../../../services/auth/auth.service';
import { forkJoin, map, of, switchMap } from 'rxjs';
import { VenueService } from '../../../services/venue/venue.service';
import { ManagesOverviewDto } from '../../../models/manages/ManagesOverviewDto';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-user-hub',
  imports: [CommonModule],
  templateUrl: './user-hub.component.html',
  styleUrl: './user-hub.component.css',
})
export class UserHubComponent implements OnInit {
  constructor(
    public managesService: ManagesService,
    public authService: AuthService,
    public venueService: VenueService
  ) {}

  public managementOverviewList: ManagesOverviewDto[] = [];

  ngOnInit(): void {
    let username = this.authService.getUsername() ?? '';

    this.managesService
      .getManagementByUsername(username)
      .pipe(
        switchMap((dtos: any) => {
          const observables = dtos.map((dto: any) =>
            forkJoin({
              venue: this.venueService.findVenueById(dto.venueId),
              manager: of(dto.managerUsername),
              management: of(dto),
            }).pipe(
              map(
                ({ venue, manager, management }) =>
                  ({
                    id: management.id,
                    venue: venue,
                    manager: manager,
                  } as ManagesOverviewDto)
              )
            )
          );

          return forkJoin(observables);
        })
      )
      .subscribe((managementOverviewList: any) => {
        this.managementOverviewList = managementOverviewList;
      });
  }
}

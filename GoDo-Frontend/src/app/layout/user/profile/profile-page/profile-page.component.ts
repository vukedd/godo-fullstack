import { Component, OnInit } from '@angular/core';
import { AvatarModule } from 'primeng/avatar';
import { CardModule } from 'primeng/card';
import { DividerModule } from 'primeng/divider';
import { ChangePasswordComponent } from '../change-password/change-password.component';
import { ButtonModule } from 'primeng/button';
import { UserProfileDto } from '../../../../models/user/UserProfileDto';
import { UserService } from '../../../../services/user/user.service';
import { AuthService } from '../../../../services/auth/auth.service';
import { MessageService } from 'primeng/api';
import { Router, RouterModule } from '@angular/router';
import { ManagesService } from '../../../../services/manages/manages.service';
import { VenueService } from '../../../../services/venue/venue.service';
import { forkJoin, map, of, switchMap } from 'rxjs';
import { ManagesOverviewDto } from '../../../../models/manages/ManagesOverviewDto';
import { CommonModule } from '@angular/common';
import { ReviewService } from '../../../../services/review/review.service';
import { ReviewCardComponent } from "../../../review/review-card/review-card.component";

@Component({
  selector: 'app-profile-page',
  imports: [
    AvatarModule,
    CardModule,
    DividerModule,
    ChangePasswordComponent,
    ButtonModule,
    RouterModule,
    CommonModule,
    ReviewCardComponent
],
  templateUrl: './profile-page.component.html',
  styleUrl: './profile-page.component.css',
})
export class ProfilePageComponent implements OnInit {
  userProfile: UserProfileDto = {
    username: '',
    email: '',
    city: '',
    address: '',
    phoneNumber: '',
    imagePath: '',
    dateOfBirth: '',
  };

  managementOverviewList: any;
  reviewList: any[] = [];

  constructor(
    public userService: UserService,
    public authService: AuthService,
    public messageService: MessageService,
    public managesService: ManagesService,
    public venueService: VenueService,
    public reviewService: ReviewService,
    public router: Router
  ) {}

  ngOnInit(): void {
    let username = this.authService.getUsername() ?? '';

    if (username == '') {
      this.messageService.add({
        severity: 'error',
        summary: 'An error has occured while fetching user profile details',
      });
      return;
    }

    this.userService
      .getUserProfileDetails()
      .subscribe({
        next: (response) => {
          this.userProfile.username = `@${response.username}`;
          this.userProfile.email = response.email;
          this.userProfile.address = response.address;
          this.userProfile.city = response.city;
          this.userProfile.dateOfBirth = response.dateOfBirth;
          this.userProfile.phoneNumber = response.phoneNumber;

          if (response.imagePath == "") {
            this.userProfile.imagePath = "https://picsum.photos/800/600";
          } else {
            this.userProfile.imagePath = response.imagePath;
          }
        },
        error: (error) => {
          this.messageService.add({
            severity: 'error',
            summary: 'An error has occured while fetching user profile details',
          });
        },
      });

      
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

    this.reviewService.getProfileReviewList().subscribe({
      next: (response) => {
        this.reviewList = response;
      },
      error: (error) => {

      }
    })
  }
}

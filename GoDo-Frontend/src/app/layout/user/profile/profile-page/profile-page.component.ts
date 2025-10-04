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

@Component({
  selector: 'app-profile-page',
  imports: [
    AvatarModule,
    CardModule,
    DividerModule,
    ChangePasswordComponent,
    ButtonModule,
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

  constructor(
    public userService: UserService,
    public authService: AuthService,
    public messageService: MessageService
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
      .getUserProfileDetails(this.authService.getUsername() ?? '')
      .subscribe({
        next: (response) => {
          this.userProfile.username = `@${response.username}`;
          this.userProfile.email = response.email;
          this.userProfile.address = response.address;
          this.userProfile.city = response.city;
          this.userProfile.dateOfBirth = response.dateOfBirth;
          this.userProfile.phoneNumber = response.phoneNumber;
          
          if (this.userProfile.imagePath == "") {
            this.userProfile.imagePath = "https://picsum.photos/800/600";
          } else {
            this.userProfile.phoneNumber = response.imagePath;
          }
        },
        error: (error) => {
          this.messageService.add({
            severity: 'error',
            summary: 'An error has occured while fetching user profile details',
          });
        },
      });
  }
}

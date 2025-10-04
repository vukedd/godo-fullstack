import { Routes } from '@angular/router';
import { RegistrationRequestManagementComponent } from './layout/admin/registration-request-management/registration-request-management.component';
import { AuthGuard } from './guards/auth-guard';
import { RoleGuard } from './guards/role-guard';
import { AddVenueComponent } from './layout/admin/add-venue/add-venue.component';
import { ExploreVenuesPageComponent } from './layout/venue/explore-venues-page/explore-venues-page.component';
import { VenuePageComponent } from './layout/venue/venue-page/venue-page.component';
import { DashboardComponent } from './layout/dashboard-related/dashboard/dashboard.component';
import { ProfileStatusGuard } from './guards/profile-status-guard';
import { CompleteProfileDetailsFormComponent } from './layout/forms/complete-profile-details-form/complete-profile-details-form.component';
import { ProfilePageComponent } from './layout/user/profile/profile-page/profile-page.component';

export const routes: Routes = [
    {
        title: "Dashboard",
        path: 'dashboard',
        component: DashboardComponent,
        canActivate: [AuthGuard, ProfileStatusGuard],
    },
    {
        title: "Registration request management",
        path: 'registration-request-management',
        component: RegistrationRequestManagementComponent,
        canActivate: [AuthGuard, RoleGuard],
        data: {
            expectedRole: "ADMIN"
        }
    },
    {
        title: "Add venue",
        path: 'add-venue',
        component: AddVenueComponent,
        canActivate: [AuthGuard, RoleGuard],
        data: {
            expectedRole: "ADMIN"
        }
    },
    {
        title: "Explore venues",
        path: 'venue',
        component: ExploreVenuesPageComponent,
        canActivate: [AuthGuard, ProfileStatusGuard],
    },
    {
        title: "Venue",
        path: 'venue/:id',
        component: VenuePageComponent,
        canActivate: [AuthGuard, ProfileStatusGuard],
    },
    {
        title: 'Finish profile',
        path: 'finish-profile',
        component: CompleteProfileDetailsFormComponent,
        canActivate: [AuthGuard]
    },
    {
        title: 'Profile',
        path: 'profile',
        component: ProfilePageComponent,
        canActivate: [AuthGuard, ProfileStatusGuard]
    }
];

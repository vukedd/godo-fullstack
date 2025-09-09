import { Routes } from '@angular/router';
import { AdminHubComponent } from './layout/admin/admin-hub/admin-hub.component';
import { RegistrationRequestManagementComponent } from './layout/admin/registration-request-management/registration-request-management.component';
import { AuthGuard } from './guards/auth-guard';
import { RoleGuard } from './guards/role-guard';
import { AddVenueComponent } from './layout/admin/add-venue/add-venue.component';

export const routes: Routes = [
    {
        title: "Admin hub",
        path: 'admin-hub',
        component: AdminHubComponent,
        canActivate: [AuthGuard, RoleGuard],
        data: {
            expectedRole: "ADMIN"
        }
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
    }
];

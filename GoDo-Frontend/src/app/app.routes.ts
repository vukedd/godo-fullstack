import { Routes } from '@angular/router';
import { AdminHubComponent } from './layout/admin/admin-hub/admin-hub.component';
import { RegistrationRequestManagementComponent } from './layout/admin/registration-request-management/registration-request-management.component';

export const routes: Routes = [
    {
        title: "Admin hub",
        path: 'admin-hub',
        component: AdminHubComponent
    },
    {
        title: "Registration request management",
        path: 'registration-request-management',
        component: RegistrationRequestManagementComponent
    }
];

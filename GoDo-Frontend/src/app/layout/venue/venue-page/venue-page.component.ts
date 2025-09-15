import { Component, OnInit } from '@angular/core';
import { VenueOverviewDto } from '../../../models/venue/VenueOverviewDto';
import { VenueService } from '../../../services/venue/venue.service';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from '../../../services/auth/auth.service';
import { CommonModule } from '@angular/common';
import { InputTextModule } from 'primeng/inputtext';
import { FloatLabel } from 'primeng/floatlabel';
import { ButtonModule } from 'primeng/button';
import { VenueType } from '../../../enums/venueType';
import {
  FormControl,
  FormGroup,
  FormsModule,
  Validators,
  ReactiveFormsModule,
} from '@angular/forms';
import { SelectModule } from 'primeng/select';
import { MessageService } from 'primeng/api';
import { ToastModule } from 'primeng/toast';
import { DialogModule } from 'primeng/dialog';

@Component({
  selector: 'app-venue-page',
  imports: [
    CommonModule,
    InputTextModule,
    FloatLabel,
    ButtonModule,
    FormsModule,
    FormsModule,
    SelectModule,
    ReactiveFormsModule,
    ToastModule,
    DialogModule,
  ],
  templateUrl: './venue-page.component.html',
  styleUrl: './venue-page.component.css',
})
export class VenuePageComponent implements OnInit {
  venue: VenueOverviewDto | undefined;
  venueType: string = '';
  isEdit: boolean = false;
  initialVenueTypeIndex: number = 0;
  isLoading: boolean = false;
  isDeleteDialogVisible: boolean = false;

  VenueTypeMap = new Map<string, string>([
    ['CULTURAL_CENTER', 'Cultural Center'],
    ['BAR', 'Bar'],
    ['NIGHT_CLUB', 'Night Club'],
    ['RESTAURANT', 'Restaurant'],
    ['THEATER', 'Theater'],
    ['ROOFTOP', 'Rooftop'],
    ['STADIUM', 'Stadium'],
    ['MUSEUM', 'Museum'],
  ]);

  public VenueTypes = [
    { name: 'Bar', value: VenueType.BAR },
    { name: 'Cultural Center', value: VenueType.CULTURAL_CENTER },
    { name: 'Museum', value: VenueType.MUSEUM },
    { name: 'Night Club', value: VenueType.NIGHT_CLUB },
    { name: 'Restaurant', value: VenueType.RESTAURANT },
    { name: 'Rooftop', value: VenueType.ROOFTOP },
    { name: 'Stadium', value: VenueType.STADIUM },
    { name: 'Theater', value: VenueType.THEATER },
  ];

  private VenueTypeConversionMap: Map<String, number> = new Map([
    ['Cultural Center', 0],
    ['Bar', 1],
    ['Night Club', 2],
    ['Restaurant', 3],
    ['Theater', 4],
    ['Rooftop', 5],
    ['Stadium', 6],
    ['Museum', 7],
  ]);

  editVenueForm: FormGroup<any> = new FormGroup({});

  constructor(
    private venueService: VenueService,
    private route: ActivatedRoute,
    private authService: AuthService,
    private messageService: MessageService,
    private router: Router
  ) {}

  ngOnInit(): void {
    let venueId = this.route.snapshot.paramMap.get('id') ?? '0';
    this.venueService.findVenueById(venueId).subscribe({
      next: (response: VenueOverviewDto) => {
        this.venue = response;

        for (let i = 0; i < this.VenueTypes.length; i++) {
          if (
            this.VenueTypes[i].name == this.VenueTypeMap.get(this.venue.type)
          ) {
            this.initialVenueTypeIndex = i;
            break;
          }
        }

        this.venueType = this.VenueTypeMap.get(this.venue.type) ?? '';

        this.editVenueForm = new FormGroup({
          name: new FormControl(this.venue?.name, Validators.required),
          address: new FormControl(this.venue?.address, Validators.required),
          description: new FormControl(
            this.venue?.description,
            Validators.required
          ),
          type: new FormControl(
            this.VenueTypes[this.initialVenueTypeIndex],
            Validators.required
          ),
        });
      },
      error: (error) => {
        this.showError(error.error.message);
      },
    });
  }

  venueTypeColor(): string {
    if (!this.venue) {
      return '#6c757d';
    }

    switch (this.venue.type) {
      case 'RESTAURANT':
        return '#AEEA00';
      case 'BAR':
        return '#FF9100';
      case 'NIGHT_CLUB':
        return '#F50057';
      case 'THEATER':
        return '#FF3D00';
      case 'STADIUM':
        return '#00E5FF';
      case 'CULTURAL_CENTER':
        return '#1DE9B6';
      case 'MUSEUM':
        return '#FFAB00';
      case 'ROOFTOP':
        return '#FFD600';
      default:
        return '#E0E0E0';
    }
  }

  isAdmin(): boolean {
    return this.authService.isAdmin();
  }

  switchToEdit() {
    this.isEdit = true;
  }

  submitChanges() {
    this.isLoading = true;
    let updateVenueRequest = {
      name: this.editVenueForm.value.name ?? '',
      address: this.editVenueForm.value.address ?? '',
      description: this.editVenueForm.value.description ?? '',
      venueType:
        this.VenueTypeConversionMap.get(this.editVenueForm.value.type.value) ??
        0,
    };

    let venueId = this.route.snapshot.params['id'];
    this.venueService.updateVenue(venueId, updateVenueRequest).subscribe({
      next: (response) => {
        if (this.venue != undefined) {
          this.venue.name = response.name;
          this.venue.address = response.address;
          this.venue.description = response.description;

          for (let i = 0; i < this.VenueTypes.length; i++) {
            if (
              this.VenueTypes[i].name == this.VenueTypeMap.get(response.venueType)
            ) {
              this.venue.type = this.VenueTypes[i].name.toUpperCase().trim().replace(" ", "_");
              this.venueType = this.VenueTypes[i].name;
              let venueTypeLabel = document.querySelector("#venueTypeLabel");
              venueTypeLabel?.setAttribute("style", `color: ${this.venueTypeColor()}`);
              break;
            }
          }
        }

        this.isLoading = false;
        this.isEdit = false;
        this.showSuccess('Venue successfully updated!');
      },
      error: (error) => {
        this.isLoading = false;
        this.isEdit = false;
        this.showError(error.error.message);
      },
    });
  }

  discardChanges() {
    this.isEdit = false;
    this.editVenueForm = new FormGroup({
          name: new FormControl(this.venue?.name, Validators.required),
          address: new FormControl(this.venue?.address, Validators.required),
          description: new FormControl(
            this.venue?.description,
            Validators.required
          ),
          type: new FormControl(
            this.VenueTypes[this.initialVenueTypeIndex],
            Validators.required
          ),
        });
  }

  deleteVenue() {
    let venueId = this.route.snapshot.paramMap.get('id') ?? '0';
    this.venueService.deleteVenue(venueId).subscribe({
      next: (response) => {
        this.router.navigate(['venue']);
        this.showSuccess('Venue successfully removed!');
      },
      error: (error) => {
        this.showError(error.error.message);
      },
    });
  }

  showDialog() {
    this.isDeleteDialogVisible = true;
  }

  hideDialog() {
    this.isDeleteDialogVisible = false;
  }

  showError(message: string) {
    this.messageService.add({
      summary: message,
      severity: 'error',
    });
  }

  showSuccess(message: string) {
    this.messageService.add({
      summary: message,
      severity: 'success',
    });
  }
}

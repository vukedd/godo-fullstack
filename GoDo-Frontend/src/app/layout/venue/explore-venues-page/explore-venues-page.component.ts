import { Component, OnInit } from '@angular/core';
import { InputTextModule } from 'primeng/inputtext';
import { ButtonModule } from 'primeng/button';
import { SelectModule } from 'primeng/select';
import { PaginatorModule, PaginatorState } from 'primeng/paginator';
import { VenueCardComponent } from '../venue-card/venue-card.component';
import { CommonModule } from '@angular/common';
import { VenueService } from '../../../services/venue/venue.service';
import { VenueOverviewDto } from '../../../models/venue/VenueOverviewDto';
import { FilterVenueDto, LogicalOperator } from '../../../models/venue/FilterVenueDto';
import { FormGroup, FormsModule, ReactiveFormsModule, FormBuilder } from '@angular/forms';
import { FloatLabelModule } from 'primeng/floatlabel';
import { InputNumberModule } from 'primeng/inputnumber';
import { RadioButtonModule } from 'primeng/radiobutton';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-explore-venues-page',
  imports: [
    InputTextModule,
    ButtonModule,
    SelectModule,
    PaginatorModule,
    VenueCardComponent,
    CommonModule,
    ReactiveFormsModule,
    FloatLabelModule,
    FormsModule,
    InputNumberModule,
    RadioButtonModule
  ],
  templateUrl: './explore-venues-page.component.html',
  styleUrl: './explore-venues-page.component.css',
})
export class ExploreVenuesPageComponent implements OnInit {
  venues: VenueOverviewDto[] = [];
  totalElements: number = 0;
  rows: number = 8;
  first: number = 0;
  filterForm!: FormGroup;
  showAdvanced: boolean = false;

  constructor(
    private venueService: VenueService,
    private fb: FormBuilder,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.filterForm = this.fb.group({
      filter: [null],
      minReviewCount: [null],
      maxReviewCount: [null],
      minRatingPerformance: [null],
      maxRatingPerformance: [null],
      minRatingAmbient: [null],
      maxRatingAmbient: [null],
      minRatingVenue: [null],
      maxRatingVenue: [null],
      minRatingOverall: [null],
      maxRatingOverall: [null],
      operator: [LogicalOperator.AND]
    });

    this.route.queryParams.subscribe(params => {
      const page = params['page'] ? +params['page'] : 0;
      this.first = page * this.rows;

      this.filterForm.patchValue({
        filter: params['filter'] || null,
        minReviewCount: params['minReviewCount'] != null ? +params['minReviewCount'] : null,
        maxReviewCount: params['maxReviewCount'] != null ? +params['maxReviewCount'] : null,
        minRatingPerformance: params['minRatingPerformance'] != null ? +params['minRatingPerformance'] : null,
        maxRatingPerformance: params['maxRatingPerformance'] != null ? +params['maxRatingPerformance'] : null,
        minRatingAmbient: params['minRatingAmbient'] != null ? +params['minRatingAmbient'] : null,
        maxRatingAmbient: params['maxRatingAmbient'] != null ? +params['maxRatingAmbient'] : null,
        minRatingVenue: params['minRatingVenue'] != null ? +params['minRatingVenue'] : null,
        maxRatingVenue: params['maxRatingVenue'] != null ? +params['maxRatingVenue'] : null,
        minRatingOverall: params['minRatingOverall'] != null ? +params['minRatingOverall'] : null,
        maxRatingOverall: params['maxRatingOverall'] != null ? +params['maxRatingOverall'] : null,
        operator: params['operator'] || LogicalOperator.AND
      }, { emitEvent: false });

      this.showAdvanced = this.hasAdvancedFilters(params);

      this.loadVenues(this.filterForm.value as FilterVenueDto, page);
    });
  }

  private hasAdvancedFilters(params: any): boolean {
    return ['minReviewCount', 'maxReviewCount',
            'minRatingPerformance', 'maxRatingPerformance',
            'minRatingAmbient', 'maxRatingAmbient',
            'minRatingVenue', 'maxRatingVenue',
            'minRatingOverall', 'maxRatingOverall']
      .some(key => params[key] != null);
  }

  toggleAdvanced() {
    this.showAdvanced = !this.showAdvanced;
  }

  onPageChange($event: PaginatorState) {
    this.router.navigate([], {
      relativeTo: this.route,
      queryParams: { page: $event.page ?? 0 },
      queryParamsHandling: 'merge'
    });
  }

  filter() {
    const v = this.filterForm.value;
    this.router.navigate([], {
      relativeTo: this.route,
      queryParams: {
        filter: v.filter || null,
        minReviewCount: v.minReviewCount ?? null,
        maxReviewCount: v.maxReviewCount ?? null,
        minRatingPerformance: v.minRatingPerformance ?? null,
        maxRatingPerformance: v.maxRatingPerformance ?? null,
        minRatingAmbient: v.minRatingAmbient ?? null,
        maxRatingAmbient: v.maxRatingAmbient ?? null,
        minRatingVenue: v.minRatingVenue ?? null,
        maxRatingVenue: v.maxRatingVenue ?? null,
        minRatingOverall: v.minRatingOverall ?? null,
        maxRatingOverall: v.maxRatingOverall ?? null,
        operator: v.operator || LogicalOperator.AND,
        page: 0
      }
    });
  }

  loadVenues(filterVenueDto: FilterVenueDto, page: number): void {
    this.venueService
      .filterVenues(filterVenueDto, page)
      .subscribe({
        next: (response) => {
          this.venues = response.content;
          this.totalElements = response.totalElements;
        },
        error: (error) => {
          console.error('Failed to load venues', error);
        },
      });
  }
}

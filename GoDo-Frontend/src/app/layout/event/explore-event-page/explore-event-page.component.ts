import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import {
  FormControl,
  FormGroup,
  FormsModule,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { InputTextModule } from 'primeng/inputtext';
import { SelectModule } from 'primeng/select';
import { SliderModule } from 'primeng/slider';
import { ButtonModule } from 'primeng/button';
import { PaginatorModule, PaginatorState } from 'primeng/paginator';
import { EventService } from '../../../services/event/event.service';
import { EventOverviewDto } from '../../../models/event/EventOverviewDto';
import { FilterEventDto } from '../../../models/event/FilterEventDto';
import { EventCardComponent } from '../event-card/event-card.component';
import { DatePickerModule } from 'primeng/datepicker';

@Component({
  selector: 'app-explore-event-page',
  imports: [
    SelectModule,
    ReactiveFormsModule,
    InputTextModule,
    SliderModule,
    CommonModule,
    ButtonModule,
    PaginatorModule,
    FormsModule,
    EventCardComponent,
    DatePickerModule,
  ],
  templateUrl: './explore-event-page.component.html',
  styleUrl: './explore-event-page.component.css',
})
export class ExploreEventPageComponent implements OnInit {
  events: EventOverviewDto[] = [];
  totalElements: number = 0;
  rows: number = 8;
  currentPage: number = 0;
  first: number = 0;

  currentType: any = { name: 'Select event type', value: -1 };
  currentFilter: string = '';
  currentPriceFrom: number = -1;
  currentPriceTo: number = -1;
  currentDate: string = '';

  isFilterFormShown = false;

  public eventTypes = [
    { name: 'Select event type', value: -1 },
    { name: 'Party', value: 0 },
    { name: 'Festival', value: 1 },
    { name: 'Stand up and theater', value: 2 },
    { name: 'Miscellaneous', value: 3 },
    { name: 'Culture', value: 4 },
    { name: 'Exhibition', value: 5 },
    { name: 'Concert', value: 6 },
    { name: 'Open mic', value: 7 },
    { name: 'Quiz', value: 8 },
    { name: 'Movie', value: 9 },
  ];

  filterForm = new FormGroup({
    filter: new FormControl(),
    priceFrom: new FormControl(),
    priceTo: new FormControl(),
    date: new FormControl(),
    eventType: new FormControl(this.eventTypes[0], Validators.required),
  });

  constructor(public eventService: EventService) {}

  ngOnInit(): void {
    this.loadEvents(
      {
        filter: '',
        priceFrom: -1,
        priceTo: -1,
        eventType: -1,
        date: '',
      },
      0
    );
  }

  toggleFilterForm() {
    this.isFilterFormShown = !this.isFilterFormShown;
  }

  clearFilterInput() {
    this.filterForm.reset();
    this.loadEvents(
      {
        filter: '',
        priceFrom: -1,
        priceTo: -1,
        eventType: -1,
        date: '',
      },
      0
    );
  }

  onPageChange($event: PaginatorState) {
    this.first = $event.first ?? 0;

    const date = this.filterForm.get('date')?.value as Date;
    let formattedDate;
    if (date != null) {
      formattedDate = date.toLocaleDateString('en-CA');
    }

    this.loadEvents(
      {
        filter: this.currentFilter,
        priceFrom: this.currentPriceFrom,
        priceTo: this.currentPriceTo,
        eventType: this.currentType.value,
        date: formattedDate?.split('T')[0] ?? '',
      },
      $event.page ?? 0
    );
  }

  filter() {
    const date = this.filterForm.get('date')?.value as Date;
    let formattedDate;
    if (date != null) {
      formattedDate = date.toLocaleDateString('en-CA');
    }
    this.currentFilter = this.filterForm.value.filter?.trim() ?? '';
    this.currentType = this.filterForm.value.eventType?.value ?? -1;
    this.currentPriceFrom = this.filterForm.value.priceFrom ?? -1;
    this.currentPriceTo = this.filterForm.value.priceTo ?? -1;
    this.currentDate = formattedDate?.split('T')[0] ?? '';

    this.loadEvents(
      {
        filter: this.currentFilter,
        priceFrom: this.currentPriceFrom,
        priceTo: this.currentPriceTo,
        eventType: this.currentType,
        date: this.currentDate,
      },
      0
    );
  }

  loadEvents(dto: FilterEventDto, pageNumber: number) {
    this.eventService.filterEvents(dto, pageNumber).subscribe({
      next: (response) => {
        this.events = response.content;
        this.totalElements = response.totalElements;
      },
      error: (error) => {
        console.log(error);
      },
    });
  }
}

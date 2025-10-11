import { CommonModule } from '@angular/common';
import { Component, Input, OnInit } from '@angular/core';
import { RouterModule } from '@angular/router';
import { ButtonModule } from 'primeng/button';
import { CardModule } from 'primeng/card';

@Component({
  selector: 'app-event-page-venue-card',
  imports: [CardModule, ButtonModule, CommonModule, RouterModule],
  templateUrl: './event-page-venue-card.component.html',
  styleUrl: './event-page-venue-card.component.css'
})
export class EventPageVenueCardComponent implements OnInit{
  @Input() venue: any;

  ngOnInit(): void {

  }
}

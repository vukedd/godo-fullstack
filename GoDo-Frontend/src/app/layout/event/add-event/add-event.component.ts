import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import {
  FormControl,
  FormGroup,
  FormsModule,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { MessageService } from 'primeng/api';
import { ButtonModule } from 'primeng/button';
import { CheckboxModule } from 'primeng/checkbox';
import { DatePickerModule } from 'primeng/datepicker';
import { FileUploadModule } from 'primeng/fileupload';
import { FloatLabelModule } from 'primeng/floatlabel';
import { InputTextModule } from 'primeng/inputtext';
import { SelectModule } from 'primeng/select';
import { TextareaModule } from 'primeng/textarea';
import { TooltipModule } from 'primeng/tooltip';
import { CreateEventDto } from '../../../models/event/createEventDto';
import { EventService } from '../../../services/event/event.service';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-add-event',
  imports: [
    FloatLabelModule,
    CommonModule,
    ReactiveFormsModule,
    SelectModule,
    FormsModule,
    InputTextModule,
    ButtonModule,
    FileUploadModule,
    TextareaModule,
    TooltipModule,
    CheckboxModule,
    DatePickerModule,
  ],
  templateUrl: './add-event.component.html',
  styleUrl: './add-event.component.css',
})
export class AddEventComponent implements OnInit {
  public eventTypes = [
    { name: 'Party', value: 0 },
    { name: 'Festival', value: 1 },
    { name: 'Stand up and theater', value: 2 },
    { name: 'Miscellaneous', value: 3 },
    { name: 'Culture', value: 4 },
    { name: 'Exhibition ', value: 5 },
    { name: 'Concert', value: 6 },
    { name: 'Open mic', value: 7 },
    { name: 'Quiz', value: 8 },
    { name: 'Movie', value: 9 },
  ];

  minDate!: Date;
  isFree: boolean = false;
  selectedFile: File | null = null;

  addEventForm!: FormGroup;

  constructor(
    public messageService: MessageService,
    public eventService: EventService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    const today = new Date();
    this.minDate = new Date(today);
    this.minDate.setDate(today.getDate() + 1);

    this.addEventForm = new FormGroup({
    name: new FormControl('', [
      Validators.required,
      Validators.minLength(3),
      Validators.maxLength(50),
    ]),
    description: new FormControl(''),
    address: new FormControl('', [
      Validators.required,
      Validators.minLength(5),
      Validators.maxLength(100),
    ]),
    type: new FormControl(this.eventTypes[0], [Validators.required]),
    eventDate: new FormControl(this.minDate, [Validators.required]),
    price: new FormControl(1, [Validators.required, Validators.min(0)]),
    isRecurrent: new FormControl(false),
  });
  }

  onUpload(event: any) {
    this.selectedFile = event.files[0];
  }

  toggleFree() {
    this.isFree = !this.isFree;
    if (this.isFree) {
      this.addEventForm.get('price')?.disable();
    } else {
      this.addEventForm.get('price')?.enable();
    }
  }

  isValid() {
    return this.addEventForm.valid && this.selectedFile != null;
  }

  submitForm() {
    if (!this.isValid()) {
      this.propagateError('Please check all field before submitting!');
    }

    let venueId = this.route.snapshot.paramMap.get('venueId') ?? '0';

    const date = this.addEventForm.get('eventDate')?.value as Date;
    const formattedDate = date.toLocaleDateString('en-CA');

    let newEventRequest: CreateEventDto = {
      name: this.addEventForm.get('name')?.value ?? '',
      description: this.addEventForm.get('description')?.value ?? '',
      date: formattedDate.split('T')[0],
      type: this.addEventForm.get('type')?.value.value,
      address: this.addEventForm.get('address')?.value,
      price: this.addEventForm.get('price')?.value,
      recurrent: this.addEventForm.get('isRecurrent')?.value,
    };

    if (this.isFree) {
      newEventRequest.price = 0;
    }

    newEventRequest.date = formattedDate;
    if (this.selectedFile != null) {
      this.eventService
        .createEvent(newEventRequest, this.selectedFile, venueId)
        .subscribe({
          next: (response) => {
            this.propagateSuccess('Event successfully created!');
            this.router.navigate(['venue', venueId]);
          },
          error: (error) => {
            this.propagateError(error.error.message);
          },
        });
    }
  }

  propagateError(message: string) {
    this.messageService.add({
      severity: 'error',
      summary: 'An error has ocurred while creating event',
      detail: message,
    });
  }

  propagateSuccess(message: string) {
    this.messageService.add({
      severity: 'success',
      summary: message,
    });
  }
}
